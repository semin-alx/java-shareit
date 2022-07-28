package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.common.controller.RestAction;
import ru.practicum.shareit.common.validator.NullOrNotEmptyConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ItemDto {

    @Data
    @AllArgsConstructor
    public static class User {
        private Long id;
        private String name;
    }

    @Data
    @AllArgsConstructor
    public static class Booking {
        private Long id;
        private Long bookerId;
    }

    @Data
    @AllArgsConstructor
    public static class Comment {
        private Long id;
        private String text;
        private String authorName;
        private LocalDateTime created;
    }

    @Null(groups = RestAction.Create.class)
    private Long id;

    @NotBlank(groups = RestAction.Create.class)
    @NullOrNotEmptyConstraint(groups = RestAction.Update.class)
    private String name;

    @NotBlank(groups = RestAction.Create.class)
    @NullOrNotEmptyConstraint(groups = RestAction.Update.class)
    private String description;

    @NotNull(groups = RestAction.Create.class)
    private Boolean available;

    @Null(groups = RestAction.Create.class)
    private ItemDto.User owner;

    private Long requestId;

    private ItemDto.Booking lastBooking;

    private ItemDto.Booking nextBooking;

    private List<ItemDto.Comment> comments;

}
