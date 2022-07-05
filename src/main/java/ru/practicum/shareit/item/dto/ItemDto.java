package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.common.controller.RestAction;
import ru.practicum.shareit.common.validator.NullOrNotEmptyConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@AllArgsConstructor
public class ItemDto {

    @Data
    @AllArgsConstructor
    public static class User {
        private int id;
        private String name;
    }

    @Null(groups = RestAction.Create.class)
    private Integer id;

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

    private Integer requestId;

}
