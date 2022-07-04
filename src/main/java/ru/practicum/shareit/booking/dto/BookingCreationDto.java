package ru.practicum.shareit.booking.dto;

import lombok.Data;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class BookingCreationDto {

    @NotNull(message = "Не указана дата начала аренды")
    @FutureOrPresent(message = "Неверное значение даты начала аренды")
    private LocalDate start;

    @NotNull(message = "Не указана дата конца аренды")
    @FutureOrPresent(message = "Неверное значение даты конца аренды")
    private LocalDate end;

    @NotNull(message = "Не указан идентификатор вещи")
    private Integer itemId;

}
