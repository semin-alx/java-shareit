package ru.practicum.shareit.booking;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.common.error_handling.exception.ItemBusyException;
import ru.practicum.shareit.common.error_handling.exception.ItemNotFoundException;
import ru.practicum.shareit.common.error_handling.exception.WrongRentPeriodException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingStorage bookingStorage;
    private final ItemService itemService;
    private final UserService userService;

    public BookingService(BookingStorage bookingStorage, ItemStorage itemStorage, ItemService itemService, ItemStorage itemStorage1, UserService userService) {
        this.bookingStorage = bookingStorage;
        this.itemService = itemService;
        this.userService = userService;
    }

    public BookingDto create(int userId, BookingDto bookingDto) {

        Item item = itemService.checkAndGetItem(bookingDto.getItemId());
        User booker = userService.checkAndGetUser(userId);

        checkAvailableForRent(bookingDto.getStart(), bookingDto.getEnd(),
                bookingDto.getItemId(), 0);

        Booking booking = BookingMapper.toBooking(bookingDto);
        booking.setItem(item);
        booking.setBooker(booker);

        booking = bookingStorage.create(booking);
        return BookingMapper.toBookingEntityDto(booking);

    }

    public BookingDto update(int bookerId, int bookingId, BookingDto bookingDto) {

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

        if (bookingNew.getStatus() != null) {
            bookingDB.setStatus(bookingNew.getStatus());
        }

        if (bookingNew.getFeedback() != null) {
            bookingDB.setFeedback(bookingNew.getFeedback());
        }

        checkAvailableForRent(bookingDB.getStart(), bookingDB.getEnd(),
                bookingDB.getItem().getId(), bookingId);

        bookingDB = bookingStorage.update(bookingDB);
        return BookingMapper.toBookingEntityDto(bookingDB);

    }

    public void delete(int id) {
        checkAndGetBooking(id);
        bookingStorage.delete(id);
    }

    public BookingDto getBookingById(int id) {
        Booking booking = checkAndGetBooking(id);
        return BookingMapper.toBookingEntityDto(booking);
    }

    public List<BookingDto> getBookingAll() {
        return bookingStorage.getBookingAll().stream()
                .map(BookingMapper::toBookingEntityDto)
                .collect(Collectors.toList());
    }


    private void checkAvailableForRent(LocalDate start, LocalDate end,
                                       int itemId, int exceptBookingId) {

        if (start.isAfter(end)) {
            throw new WrongRentPeriodException("Период аренды указан неверно");
        }

        List<Booking> bookingList = bookingStorage.getBookingByItem(itemId);

        // Ищем аренду, которая пересекается с нашей
        Optional<Booking> hinder = bookingList.stream().filter(b -> {

            if (b.getId() == exceptBookingId) {
                return false;
            }

            if (b.getStatus() != BookingStatus.APPROVED) {
                return false;
            }

            return (start.compareTo(b.getEnd()) < 0) && (end.compareTo(b.getStart()) >= 0);

        }).findFirst();

        if (hinder.isPresent()) {
            throw new ItemBusyException("Вещь на указанный период недоступна");
        }

    }

    public Booking checkAndGetBooking(int id) {
        Optional<Booking> booking = bookingStorage.getById(id);
        if (!booking.isPresent()) {
            throw new ItemNotFoundException("Заказ по идентификатору не найден");
        } else {
            return booking.get();
        }
    }

}
