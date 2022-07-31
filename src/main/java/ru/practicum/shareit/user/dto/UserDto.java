package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.common.controller.RestAction;
import ru.practicum.shareit.common.validator.NullOrNotEmptyConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@AllArgsConstructor
public class UserDto {

    @Null(groups = {RestAction.Create.class, RestAction.Update.class})
    private Long id;

    @NotBlank(groups = RestAction.Create.class)
    @NullOrNotEmptyConstraint(groups = RestAction.Update.class)
    private String name;

    @NotNull(groups = RestAction.Create.class)
    @Email
    private String email;

    public UserDto() {

    }

}
