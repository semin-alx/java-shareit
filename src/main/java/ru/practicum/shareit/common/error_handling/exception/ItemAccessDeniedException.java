package ru.practicum.shareit.common.error_handling.exception;

public class ItemAccessDeniedException extends RuntimeException {
    public ItemAccessDeniedException(String message) {
        super(message);
    }
}

