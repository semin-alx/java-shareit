package ru.practicum.shareit.common.error_handling.exception;

public class BookingStatusAlreadySetException extends RuntimeException {
    public BookingStatusAlreadySetException(String message) {
        super(message);
    }
}
