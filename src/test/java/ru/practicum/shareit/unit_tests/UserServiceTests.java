package ru.practicum.shareit.unit_tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.common.error_handling.exception.UserNotFoundException;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.UserServiceImpl;

import java.util.Optional;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    UserRepository mockUserRepository;

    @Test
    void user_getUserById_not_found() {

        UserService userService = new UserServiceImpl(mockUserRepository);

        Mockito
                .when(mockUserRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        final UserNotFoundException exception = Assertions.assertThrows(
                UserNotFoundException.class,
                () -> userService.getUserById(1));

        Assertions.assertEquals("Пользователь с таким id не найден", exception.getMessage());
    }

    @Test
    void user_delete_not_found() {

        UserService userService = new UserServiceImpl(mockUserRepository);

        Mockito
                .when(mockUserRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        final UserNotFoundException exception = Assertions.assertThrows(
                UserNotFoundException.class,
                () -> userService.delete(1));

        Assertions.assertEquals("Пользователь с таким id не найден", exception.getMessage());
    }

    @Test
    void user_update_not_found() {

        UserService userService = new UserServiceImpl(mockUserRepository);

        Mockito
                .when(mockUserRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        final UserNotFoundException exception = Assertions.assertThrows(
                UserNotFoundException.class,
                () -> userService.update(1, null));

        Assertions.assertEquals("Пользователь с таким id не найден",
                exception.getMessage());
    }


}
