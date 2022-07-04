package ru.practicum.shareit.common.error_handling.exception;

public class WrongRentPeriodException extends RuntimeException {
    public WrongRentPeriodException(String message) {
        super(message);
    }
}
