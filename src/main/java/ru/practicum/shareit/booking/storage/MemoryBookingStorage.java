package ru.practicum.shareit.booking.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.common.memory_storage.MemoryBaseStorage;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MemoryBookingStorage extends MemoryBaseStorage implements BookingStorage {

    @Override
    public Booking create(Booking booking) {
        int newId = add(booking);
        booking.setId(newId);
        return booking;
    }

    @Override
    public Booking update(Booking booking) {
        getObjects().put(booking.getId(), booking);
        return booking;
    }

    @Override
    public List<Booking> getBookingAll() {
        return getObjects().values().stream()
                .map(o -> (Booking)o)
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> getBookingByItem(int itemId) {
        return getObjects().values().stream()
                .map(o -> (Booking)o)
                .filter(i -> i.getItem().getId() == itemId)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Booking> getById(int id) {

        Booking booking = (Booking)getObjects().get(id);

        if (booking == null) {
            return Optional.empty();
        } else {
            return Optional.of(booking);
        }

    }

}
