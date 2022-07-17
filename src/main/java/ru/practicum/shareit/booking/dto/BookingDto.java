package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.common.controller.RestAction;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingDto {

    @Data
    @AllArgsConstructor
    public static class User {
        private Long id;
        private String name;
    }

    @Data
    @AllArgsConstructor
    public static class Item {
        private Long id;
        private String name;
    }

    @Null(groups = {RestAction.Create.class, RestAction.Update.class})
    private Long id;

    @NotNull(groups = RestAction.Create.class)
    @FutureOrPresent
    private LocalDateTime start;

    @NotNull(groups = RestAction.Create.class)
    @FutureOrPresent
    private LocalDateTime end;

    @NotNull(groups = RestAction.Create.class)
    private Long itemId;

    @Null(groups = {RestAction.Create.class, RestAction.Update.class})
    private BookingDto.User booker;

    @Null(groups = {RestAction.Create.class, RestAction.Update.class})
    private BookingDto.Item item;

    @Null(groups = {RestAction.Create.class, RestAction.Update.class})
    private BookingStatus status;

}
