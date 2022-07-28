package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

public class BookingMapper {

    public static BookingDto toBookingEntityDto(Booking booking) {

        BookingDto.User booker = new BookingDto.User(booking.getBooker().getId(),
                                                     booking.getBooker().getName());

        BookingStatus status = BookingStatus.get(booking.getIsApproved(), booking.getIsCanceled());

        BookingDto.Item item = new BookingDto.Item(booking.getItem().getId(),
                booking.getItem().getName());

        return new BookingDto(booking.getId(), booking.getStart(), booking.getEnd(),
                booking.getItem().getId(), booker, item, status);

    }

    public static Booking toBooking(BookingDto bookingDto) {

        return new Booking(bookingDto.getId(), bookingDto.getStart(),
                bookingDto.getEnd(), null, null, false, false);

    }

}
