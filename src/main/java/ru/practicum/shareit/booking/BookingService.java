package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import java.util.List;

public interface BookingService {

    BookingDto create(long userId, BookingDto bookingDto);

    BookingDto update(long bookerId, long bookingId, BookingDto bookingDto);

    void delete(long id);

    BookingDto getBookingById(long userId, long id);

    BookingDto changeStatus(long ownerId, long bookingId, boolean approved);

    List<BookingDto> getBookingByBooker(long bookerId, BookingFilterState state);

    List<BookingDto> getBookingByBooker(long bookerId, BookingFilterState state, int from, int page);

    Booking checkAndGetBooking(long id);

    List<BookingDto> getBookingByOwner(long ownerId, BookingFilterState state);

    List<BookingDto> getBookingByOwner(long ownerId, BookingFilterState state, int from, int page);

}
