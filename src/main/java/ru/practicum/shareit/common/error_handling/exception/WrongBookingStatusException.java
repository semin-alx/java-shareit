package ru.practicum.shareit.common.error_handling.exception;

public class WrongBookingStatusException extends RuntimeException {

    public WrongBookingStatusException(String message) {
        super(message);
    }

}

