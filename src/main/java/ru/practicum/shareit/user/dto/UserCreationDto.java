package ru.practicum.shareit.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserCreationDto {

    @NotNull(message = "Не указано имя пользователя")
    @NotBlank(message = "Имя пользователя не должно быть пустым")
    private String name;

    @NotNull(message = "Email пользователя должен быть указан")
    @NotBlank(message = "Email пользователя не должен быть пустым")
    @Email(message = "Email пользователя указан неверно")
    private String email;

}
