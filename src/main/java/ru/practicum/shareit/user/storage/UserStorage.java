package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    User create(User user);

    User update(User user);

    void delete(int id);

    Optional<User> findByEmail(String email);

    Optional<User> findByName(String name);

    Optional<User> getById(int id);

    List<User> getUsers();

}
