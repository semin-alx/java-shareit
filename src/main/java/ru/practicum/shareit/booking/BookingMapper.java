package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

public class BookingMapper {

    public static BookingDto toBookingEntityDto(Booking booking) {

        BookingDto.Item item = new BookingDto.Item(booking.getItem().getId(),
                                                               booking.getItem().getName());

        BookingDto.User booker = new BookingDto.User(booking.getBooker().getId(),
                                                                 booking.getBooker().getName());

        return new BookingDto(booking.getId(), booking.getStart(), booking.getEnd(),
                item, booker, booking.getStatus(), booking.getFeedback());

    }

}
