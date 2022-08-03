package ru.practicum.shareit.booking;

public enum BookingFilterState {
    ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED;

    public static BookingFilterState parse(String value) {
        return BookingFilterState.valueOf(value);
    }

}
