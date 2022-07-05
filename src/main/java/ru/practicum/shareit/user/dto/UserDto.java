package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.common.controller.RestAction;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@AllArgsConstructor
public class UserDto {

    private Integer id;

    @NotBlank(groups = RestAction.Create.class)
    private String name;

    @NotNull(groups = RestAction.Create.class)
    @Email
    private String email;

}
