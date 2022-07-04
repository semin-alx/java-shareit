package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserEntityDto;

public class UserMapper {
    public static UserEntityDto toUserEntityDto(User user) {
        return new UserEntityDto(user.getId(), user.getName(), user.getEmail());
    }
}
