package ru.practicum.shareit.booking.storage;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface BookingStorage {

    Booking create(Booking booking);
    Booking update(Booking booking);
    void delete(int bookingId);

    List<Booking> getBookingAll();
    List<Booking> getBookingByItem(int itemId);

    Optional<Booking> getById(int id);

}
