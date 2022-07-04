package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.common.memory_storage.MemoryBaseStorage;
import ru.practicum.shareit.user.User;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class MemoryUserStorage extends MemoryBaseStorage implements UserStorage {

    @Override
    public User create(User user) {
        int newId = add(user);
        user.setId(newId);
        return user;
    }

    @Override
    public User update(User user) {
        getObjects().put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return getObjects().values().stream()
                .map(o -> (User)o)
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    @Override
    public Optional<User> findByName(String name) {
        return getObjects().values().stream()
                .map(o -> (User)o)
                .filter(u -> u.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    @Override
    public Optional<User> getById(int id) {

        User user = (User)get(id);

        if (user == null) {
            return Optional.empty();
        } else {
            return Optional.of(user);
        }

    }

    @Override
    public List<User> getUsers() {
        return getObjects().values().stream()
                .map(o -> (User)o)
                .collect(Collectors.toList());
    }

}
