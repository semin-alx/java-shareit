package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.error_handling.exception.UserAlreadyExistsException;
import ru.practicum.shareit.common.error_handling.exception.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
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

    public UserDto create(UserDto userDto) {

        User user = UserMapper.toUser(userDto);

        if (userStorage.findByEmail(user.getName()).isPresent()) {
            throw new UserAlreadyExistsException("Пользователь с таким именем уже существует");
        }

        if (userStorage.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Пользователь с таким email уже существует");
        }

        user = userStorage.create(user);
        return UserMapper.toUserDto(user);

    }

    public UserDto update(int userId, UserDto userDto) {

        User userDB = checkAndGetUser(userId);
        User userNew = UserMapper.toUser(userDto);

        Optional<User> user1;

        if (userNew.getName() != null) {
            user1 = userStorage.findByName(userNew.getName());
            if (user1.isPresent() && (user1.get().getId() != userId)) {
                throw new UserAlreadyExistsException("Пользователь с таким именем уже существует");
            } else {
                userDB.setName(userNew.getName());
            }
        }

        if (userNew.getEmail() != null) {
            user1 = userStorage.findByEmail(userNew.getEmail());
            if (user1.isPresent() && (user1.get().getId() != userId)) {
                throw new UserAlreadyExistsException("Пользователь с таким email уже существует");
            } else {
                userDB.setEmail(userDto.getEmail());
            }
        }

        userDB = userStorage.update(userDB);

        return UserMapper.toUserDto(userDB);

    }

    public void delete(int id) {
        checkAndGetUser(id);
        userStorage.delete(id);
    }

    public UserDto getUserById(int id) {
        User user = checkAndGetUser(id);
        return UserMapper.toUserDto(user);
    }

    public List<UserDto> getUsers() {
        return userStorage.getUsers().stream()
                .map(UserMapper::toUserDto)
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
