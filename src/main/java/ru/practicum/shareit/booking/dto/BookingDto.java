package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.common.controller.RestAction;
import ru.practicum.shareit.common.validator.NullOrNotEmptyConstraint;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class BookingDto {

    @Data
    @AllArgsConstructor
    public static class User {
        private int id;
        private String name;
    }

    @Null(groups = {RestAction.Create.class, RestAction.Update.class})
    private Integer id;

    @NotNull(groups = RestAction.Create.class)
    @FutureOrPresent
    private LocalDate start;

    @NotNull(groups = RestAction.Create.class)
    @FutureOrPresent
    private LocalDate end;

    @NotNull(groups = RestAction.Create.class)
    private Integer itemId;

    @Null(groups = {RestAction.Create.class, RestAction.Update.class})
    private BookingDto.User booker;

    @NotNull(groups = RestAction.Create.class)
    private BookingStatus status;

    @Null(groups = RestAction.Create.class)
    @NullOrNotEmptyConstraint(groups = RestAction.Update.class)
    private String feedback;

}
