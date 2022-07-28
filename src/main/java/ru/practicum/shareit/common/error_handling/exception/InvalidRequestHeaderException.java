package ru.practicum.shareit.common.error_handling.exception;

public class InvalidRequestHeaderException extends RuntimeException {
    public InvalidRequestHeaderException(String message) {
        super(message);
    }
}

