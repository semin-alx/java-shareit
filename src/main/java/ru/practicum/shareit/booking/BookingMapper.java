package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingEntityDto;

public class BookingMapper {

    public static BookingEntityDto toBookingEntityDto(Booking booking) {

        BookingEntityDto.Item item = new BookingEntityDto.Item(booking.getItem().getId(),
                                                               booking.getItem().getName());

        BookingEntityDto.User booker = new BookingEntityDto.User(booking.getBooker().getId(),
                                                                 booking.getBooker().getName());

        return new BookingEntityDto(booking.getId(), booking.getStart(), booking.getEnd(),
                item, booker, booking.getStatus(), booking.getFeedback());

    }

}
