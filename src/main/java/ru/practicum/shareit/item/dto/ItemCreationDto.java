package ru.practicum.shareit.item.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ItemCreationDto {

    @NotBlank(message = "Имя вещи не должно быть пустым")
    private String name;

    @NotBlank(message = "Описание вещи не должно быть пустым")
    private String description;

    @NotNull(message = "Статус не указан")
    private Boolean available;

    private Integer requestId;

}
