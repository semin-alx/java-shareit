package ru.practicum.shareit.common.error_handling.exception;

public class ItemNotAvailableException  extends RuntimeException {
    public ItemNotAvailableException(String message) {
        super(message);
    }
}

