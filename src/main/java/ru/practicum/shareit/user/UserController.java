package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.controller.RestAction;
import ru.practicum.shareit.user.dto.UserDto;

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
    @Validated({RestAction.Create.class})
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        return userService.create(userDto);
    }

    @PatchMapping(value = "/{id}")
    @Validated({RestAction.Update.class})
    public UserDto update(@PathVariable long id, @Valid @RequestBody UserDto userDto) {
        return userService.update(id, userDto);
    }

    @GetMapping
    public List<UserDto> getUsers() {
        return userService.getUsers();
    }

    @GetMapping(value = "/{id}")
    public UserDto getUser(@PathVariable int id) {
        return userService.getUserById(id);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable int id) {
        userService.delete(id);
    }


}
