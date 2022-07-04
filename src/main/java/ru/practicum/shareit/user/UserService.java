package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.error_handling.exception.UserAlreadyExistsException;
import ru.practicum.shareit.common.error_handling.exception.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserCreationDto;
import ru.practicum.shareit.user.dto.UserEntityDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public UserEntityDto create(UserCreationDto userDto) {

        User user = new User(null, userDto.getName(), userDto.getEmail());

        if (userStorage.findByEmail(user.getName()).isPresent()) {
            throw new UserAlreadyExistsException("Пользователь с таким именем уже существует");
        }

        if (userStorage.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Пользователь с таким email уже существует");
        }

        user = userStorage.create(user);
        return UserMapper.toUserEntityDto(user);

    }

    public UserEntityDto update(int userId, UserUpdateDto userDto) {

        User user0 = checkAndGetUser(userId);
        Optional<User> user1;

        if (userDto.getName() != null) {
            user1 = userStorage.findByName(userDto.getName());
            if (user1.isPresent() && (user1.get().getId() != userId)) {
                throw new UserAlreadyExistsException("Пользователь с таким именем уже существует");
            } else {
                user0.setName(userDto.getName());
            }
        }

        if (userDto.getEmail() != null) {
            user1 = userStorage.findByEmail(userDto.getEmail());
            if (user1.isPresent() && (user1.get().getId() != userId)) {
                throw new UserAlreadyExistsException("Пользователь с таким email уже существует");
            } else {
                user0.setEmail(userDto.getEmail());
            }
        }

        userStorage.update(user0);

        return UserMapper.toUserEntityDto(user0);

    }

    public void delete(int id) {
        checkAndGetUser(id);
        userStorage.delete(id);
    }

    public UserEntityDto getUserById(int id) {
        User user = checkAndGetUser(id);
        return UserMapper.toUserEntityDto(user);
    }

    public List<UserEntityDto> getUsers() {
        return userStorage.getUsers().stream()
                .map(UserMapper::toUserEntityDto)
                .collect(Collectors.toList());
    }

    public User checkAndGetUser(int id) {
        Optional<User> user = userStorage.getById(id);
        if (!user.isPresent()) {
            throw new UserNotFoundException("Пользователь с таким id не найден");
        } else {
            return user.get();
        }
    }

}
