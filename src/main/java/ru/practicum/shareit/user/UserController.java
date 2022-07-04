package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreationDto;
import ru.practicum.shareit.user.dto.UserEntityDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@Validated
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserEntityDto create(@Valid @RequestBody UserCreationDto userDto) {
        return userService.create(userDto);
    }

    @PatchMapping(value = "/{id}")
    public UserEntityDto update(@PathVariable int id, @Valid @RequestBody UserUpdateDto userDto) {
        return userService.update(id, userDto);
    }

    @GetMapping
    public List<UserEntityDto> getUsers() {
        return userService.getUsers();
    }

    @GetMapping(value = "/{id}")
    public UserEntityDto getUser(@PathVariable int id) {
        return userService.getUserById(id);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable int id) {
        userService.delete(id);
    }


}
