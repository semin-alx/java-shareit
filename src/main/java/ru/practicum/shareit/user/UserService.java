package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import java.util.List;

public interface UserService {

    UserDto create(UserDto userDto);

    UserDto update(long userId, UserDto userDto);

    void delete(long id);

    UserDto getUserById(long id);

    List<UserDto> getUsers();

    User checkAndGetUser(long id);

}
