package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.BookingStatus;

import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDate;

@Data
public class BookingUpdateDto {

    @FutureOrPresent(message = "Неверное значение даты начала аренды")
    private LocalDate start;

    @FutureOrPresent(message = "Неверное значение даты конца аренды")
    private LocalDate end;

    private Integer itemId;
    private BookingStatus status;
    private String feedback;

}
