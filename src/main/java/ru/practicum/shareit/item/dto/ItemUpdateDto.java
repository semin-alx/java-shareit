package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.common.validator.NullOrNotEmptyConstraint;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ItemUpdateDto {

    @NullOrNotEmptyConstraint(message = "Имя вещи не должно быть пустым")
    private String name;

    @NullOrNotEmptyConstraint(message = "Описание вещи не должно быть пустым")
    private String description;

    private Boolean available;

}