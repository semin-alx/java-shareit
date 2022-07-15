package ru.practicum.shareit.common.error_handling.exception;

public class ItemBusyException extends RuntimeException {
    public ItemBusyException(String message) {
        super(message);
    }
}
