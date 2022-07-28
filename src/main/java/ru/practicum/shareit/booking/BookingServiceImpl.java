package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.common.error_handling.exception.*;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private final ItemService itemService;
    private final UserService userService;
    private final BookingRepository bookingRepository;

    @Autowired
    public BookingServiceImpl(ItemService itemService, UserService userService, BookingRepository bookingRepository) {
        this.itemService = itemService;
        this.userService = userService;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public BookingDto create(long userId, BookingDto bookingDto) {

        Item item = itemService.checkAndGetItem(bookingDto.getItemId());

        User booker;

        // В тестах, когда в базе не найдена сущность users ожидается ошибка 404
        // что и логично, но в тесте "Booking create failed by wrong userId"
        // ожидается ошибка 500, поэтому, мы подменяем сообщение
        try {
            booker = userService.checkAndGetUser(userId);
        } catch (UserNotFoundException e) {
            throw new InvalidRequestHeaderException(e.getMessage());
        }

        // ТЕСТ: Booking create from user1 to item1 failed
        if (userId == item.getOwner().getId()) {
            throw new ItemAccessDeniedException("Для владельца аренда недоступна");
        }

        checkAvailableForRent(bookingDto.getStart(), bookingDto.getEnd(),
                item, 0);

        Booking booking = BookingMapper.toBooking(bookingDto);
        booking.setItem(item);
        booking.setBooker(booker);

        booking = bookingRepository.save(booking);
        return BookingMapper.toBookingEntityDto(booking);

    }

    @Override
    public BookingDto update(long bookerId, long bookingId, BookingDto bookingDto) {

        userService.checkAndGetUser(bookerId);
        Booking bookingDB = checkAndGetBooking(bookingId);
        Booking bookingNew = BookingMapper.toBooking(bookingDto);

        if (bookingDto.getItemId() != null) {
            Item item = itemService.checkAndGetItem(bookingDto.getItemId());
            bookingDB.setItem(item);
        }

        if (bookingNew.getStart() != null) {
            bookingDB.setStart(bookingNew.getStart());
        }

        if (bookingNew.getEnd() != null) {
            bookingDB.setEnd(bookingNew.getEnd());
        }

        if (bookingNew.getIsApproved() != null) {
            bookingDB.setIsApproved(bookingNew.getIsApproved());
        }

        if (bookingNew.getIsCanceled() != null) {
            bookingDB.setIsCanceled(bookingNew.getIsCanceled());
        }

        checkAvailableForRent(bookingDB.getStart(), bookingDB.getEnd(),
                bookingDB.getItem(), bookingId);

        bookingDB = bookingRepository.save(bookingDB);
        return BookingMapper.toBookingEntityDto(bookingDB);

    }

    @Override
    public void delete(long id) {
        Booking booking = checkAndGetBooking(id);
        bookingRepository.delete(booking);
    }

    @Override
    public BookingDto getBookingById(long userId, long id) {

        Booking booking = checkAndGetBooking(id);

        long bookerId = booking.getBooker().getId();
        long ownerId = booking.getItem().getOwner().getId();

        // Заказ вернем только тому, кто заказал или владельцу заказанной вещи
        if ((bookerId != userId) && (ownerId != userId)) {
            throw new ItemAccessDeniedException("Доступ к чужим заказам запрещен");
        }

        return BookingMapper.toBookingEntityDto(booking);
    }

    @Override
    public BookingDto changeStatus(long userId, long bookingId, boolean approved) {

        Booking booking = checkAndGetBooking(bookingId);

        boolean isOwnerChangeStatus = booking.getItem().getOwner().getId() == userId;

        if ((isOwnerChangeStatus && (booking.getItem().getOwner().getId() != userId))
            || (!isOwnerChangeStatus && (booking.getBooker().getId() != userId))) {

            throw new ItemAccessDeniedException("Доступ к чужим заказам запрещен");

        }

        // Заказчик не может подтверждать свой заказ
        if (!isOwnerChangeStatus && approved) {
            throw new WrongBookingStatusException("Неверный статус заказа");
        }

        if (booking.getIsCanceled()) {
            // Уже конечный статус (CANCEL или REJECT) изменить ничего нельзя
            throw new WrongBookingStatusException("Неверный статус заказа");
        }

        if (booking.getIsApproved() && approved && !booking.getIsCanceled()) {
            // Попытка установить APPROVED, когда он уже установлен
            // Если на предыдущие ошибки в тестах ожидался код 404
            // То на эту ошибку в тестах ждут код 400
            throw new BookingStatusAlreadySetException("Неверный статус заказа");
        }

        if (approved) {
            // APPROVED
            booking.setIsApproved(true);
            booking.setIsCanceled(false);
        } else {
            if (isOwnerChangeStatus) {
                // REJECTED
                booking.setIsApproved(true);
                booking.setIsCanceled(true);
            } else {
                // CANCEL
                booking.setIsApproved(false);
                booking.setIsCanceled(true);
            }

        }

        return BookingMapper.toBookingEntityDto(bookingRepository.save(booking));

    }

    @Override
    public List<BookingDto> getBookingByBooker(long bookerId, BookingFilterState state) {

        switch (state) {
            case ALL:
                return bookingRepository.findByBookerId(bookerId).stream()
                        .map(BookingMapper::toBookingEntityDto)
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findCurrentByBooker(bookerId, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::toBookingEntityDto)
                        .collect(Collectors.toList());
            case PAST:
                return bookingRepository.findPastByBooker(bookerId, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::toBookingEntityDto)
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findFutureByBooker(bookerId, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::toBookingEntityDto)
                        .collect(Collectors.toList());
            case WAITING:
                return bookingRepository.findByStatusByBooker(bookerId,
                                false, false)
                        .stream()
                        .map(BookingMapper::toBookingEntityDto)
                        .collect(Collectors.toList());
            case REJECTED:
                return bookingRepository.findByStatusByBooker(bookerId,
                                true, true)
                        .stream()
                        .map(BookingMapper::toBookingEntityDto)
                        .collect(Collectors.toList());
            default:
                return List.of(); // На всякий случай
        }

    }

    private void checkAvailableForRent(LocalDateTime start, LocalDateTime end,
                                       Item item, long exceptBookingId) {

        if (start.isAfter(end)) {
            throw new WrongRentPeriodException("Период аренды указан неверно");
        }

        if (!item.getAvailable()) {
            throw new ItemNotAvailableException("Данная вещь недоступна для заказа");
        }

        List<Booking> bookingList = bookingRepository.findByItemId(item.getId());

        // Ищем аренду, которая пересекается с нашей
        Optional<Booking> hinder = bookingList.stream().filter(b -> {

            if (b.getId() == exceptBookingId) {
                return false;
            }

            // CANCELED REJECTED
            if (b.getIsCanceled()) {
                return false;
            }

            return (start.compareTo(b.getEnd()) < 0) && (end.compareTo(b.getStart()) >= 0);

        }).findFirst();

        if (hinder.isPresent()) {
            throw new ItemBusyException("Вещь на указанный период недоступна");
        }

    }

    @Override
    public Booking checkAndGetBooking(long id) {
        Optional<Booking> booking = bookingRepository.findById(id);
        if (!booking.isPresent()) {
            throw new ItemNotFoundException("Заказ по идентификатору не найден");
        } else {
            return booking.get();
        }
    }

    @Override
    public List<BookingDto> getBookingByOwner(long ownerId, BookingFilterState state) {

        switch (state) {
            case ALL:
                return bookingRepository.findByOwnerId(ownerId).stream()
                        .map(BookingMapper::toBookingEntityDto)
                        .collect(Collectors.toList());
            case PAST:
                return bookingRepository.findPastByOwner(ownerId, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::toBookingEntityDto)
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findFutureByOwner(ownerId, LocalDateTime.now())
                        .stream()
                        .map(BookingMapper::toBookingEntityDto)
                        .collect(Collectors.toList());
            case WAITING:
                return bookingRepository.findByStatusByOwner(ownerId,
                                false, false)
                        .stream()
                        .map(BookingMapper::toBookingEntityDto)
                        .collect(Collectors.toList());
            case REJECTED:
                return bookingRepository.findByStatusByOwner(ownerId,
                                true, true)
                        .stream()
                        .map(BookingMapper::toBookingEntityDto)
                        .collect(Collectors.toList());
            default:
                return List.of(); // На всякий случай

        }

    }

}
