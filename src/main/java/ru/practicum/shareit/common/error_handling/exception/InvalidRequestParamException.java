package ru.practicum.shareit.common.error_handling.exception;

public class InvalidRequestParamException extends RuntimeException {
    public InvalidRequestParamException(String message) {
        super(message);
    }
}
