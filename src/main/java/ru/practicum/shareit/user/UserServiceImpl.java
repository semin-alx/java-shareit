package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.error_handling.exception.UserNotFoundException;
import ru.practicum.shareit.common.error_handling.exception.UserSaveException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto create(UserDto userDto) {

        User user = UserMapper.toUser(userDto);

        try {
            user = userRepository.save(user);
        } catch (Exception e) {
            throw new UserSaveException(e.getMessage());
        }

        return UserMapper.toUserDto(user);

    }

    @Override
    public UserDto update(int userId, UserDto userDto) {

        User userDB = checkAndGetUser(userId);
        User userNew = UserMapper.toUser(userDto);

        if (userNew.getName() != null) {
            userDB.setName(userNew.getName());
        }

        if (userNew.getEmail() != null) {
            userDB.setEmail(userDto.getEmail());
        }

        try {
            userDB = userRepository.save(userDB);
        } catch (Exception e) {
            throw new UserSaveException(e.getMessage());
        }

        return UserMapper.toUserDto(userDB);

    }

    @Override
    public void delete(long id) {
        User user = checkAndGetUser(id);
        userRepository.delete(user);
    }

    @Override
    public UserDto getUserById(long id) {
        User user = checkAndGetUser(id);
        return UserMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> getUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public User checkAndGetUser(long id) {

        Optional<User> user = userRepository.findById(id);

        if (!user.isPresent()) {
            throw new UserNotFoundException("Пользователь с таким id не найден");
        } else {
            return user.get();
        }

    }

}
