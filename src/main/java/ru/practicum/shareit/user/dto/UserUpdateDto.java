package ru.practicum.shareit.user.dto;

import lombok.Data;
import ru.practicum.shareit.common.validator.NullOrNotEmptyConstraint;
import javax.validation.constraints.Email;

@Data
public class UserUpdateDto {

    @NullOrNotEmptyConstraint(message = "Имя пользователя не должно быть пустым")
    private String name;

    @Email(message = "Email пользователя указан неверно")
    private String email;

}
