package ru.practicum.shareit.booking;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.dto.BookingEntityDto;
import ru.practicum.shareit.booking.dto.BookingUpdateDto;
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

    private final String ERR_WRONG_RENT_PERIOD = "Период аренды указан неверно";
    private final String ERR_ITEM_IS_BUSY = "Вещь на указанный период недоступна";
    private final String ERR_BOOKING_BY_ID_NOT_FOUND = "Заказ по идентификатору не найден";

    private final BookingStorage bookingStorage;
    private final ItemService itemService;
    private final UserService userService;

    public BookingService(BookingStorage bookingStorage, ItemStorage itemStorage, ItemService itemService, ItemStorage itemStorage1, UserService userService) {
        this.bookingStorage = bookingStorage;
        this.itemService = itemService;
        this.userService = userService;
    }

    public BookingEntityDto create(int userId, BookingCreationDto bookingDto) {

        Item item = itemService.checkAndGetItem(bookingDto.getItemId());
        User booker = userService.checkAndGetUser(userId);

        checkAvailableForRent(bookingDto.getStart(), bookingDto.getEnd(),
                bookingDto.getItemId(), 0);

        Booking booking = new Booking(null, bookingDto.getStart(),
                bookingDto.getEnd(), item, booker, BookingStatus.WAITING, null);

        booking = bookingStorage.create(booking);
        return BookingMapper.toBookingEntityDto(booking);

    }

    public BookingEntityDto update(int bookerId, int bookingId, BookingUpdateDto bookingDto) {

        User booker = userService.checkAndGetUser(bookerId);
        Booking booking = checkAndGetBooking(bookingId);

        if (bookingDto.getItemId() != null) {
            Item item = itemService.checkAndGetItem(bookingDto.getItemId());
            booking.setItem(item);
        }

        if (bookingDto.getStart() != null) {
            booking.setStart(bookingDto.getStart());
        }

        if (bookingDto.getEnd() != null) {
            booking.setEnd(bookingDto.getEnd());
        }

        if (bookingDto.getStatus() != null) {
            booking.setStatus(bookingDto.getStatus());
        }

        if (bookingDto.getFeedback() != null) {
            booking.setFeedback(bookingDto.getFeedback());
        }

        checkAvailableForRent(booking.getStart(), booking.getEnd(),
                booking.getItem().getId(), bookingId);

        booking = bookingStorage.update(booking);
        return BookingMapper.toBookingEntityDto(booking);

    }

    public void delete(int id) {
        checkAndGetBooking(id);
        bookingStorage.delete(id);
    }

    public BookingEntityDto getBookingById(int id) {
        Booking booking = checkAndGetBooking(id);
        return BookingMapper.toBookingEntityDto(booking);
    }

    public List<BookingEntityDto> getBookingAll() {
        return bookingStorage.getBookingAll().stream()
                .map(BookingMapper::toBookingEntityDto)
                .collect(Collectors.toList());
    }


    private void checkAvailableForRent(LocalDate start, LocalDate end,
                                       int itemId, int exceptBookingId) {

        if (start.isAfter(end)) {
            throw new WrongRentPeriodException(ERR_WRONG_RENT_PERIOD);
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

            if ((start.compareTo(b.getEnd()) >= 0) || (end.compareTo(b.getStart()) < 0)) {
                return false;
            }

            return true;

        }).findFirst();

        if (hinder.isPresent()) {
            throw new ItemBusyException(ERR_ITEM_IS_BUSY);
        }

    }

    public Booking checkAndGetBooking(int id) {
        Optional<Booking> booking = bookingStorage.getById(id);
        if (!booking.isPresent()) {
            throw new ItemNotFoundException(ERR_BOOKING_BY_ID_NOT_FOUND);
        } else {
            return booking.get();
        }
    }

}
