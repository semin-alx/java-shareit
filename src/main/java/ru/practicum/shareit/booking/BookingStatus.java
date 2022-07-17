package ru.practicum.shareit.booking;

public enum BookingStatus {
    WAITING, APPROVED, REJECTED, CANCELED;

    public static BookingStatus get(boolean isApproved, boolean isCanceled) {

        if (!isApproved && !isCanceled) {
            return WAITING;
        } else if (isApproved && !isCanceled) {
            return APPROVED;
        } else if (!isApproved && isCanceled) {
            return CANCELED;
        } else {
            return REJECTED;
        }

    }

}
