package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;
import java.util.List;
import java.util.Optional;

public interface ItemStorage {

    Item create(Item item);
    Item update(Item item);
    void delete(int id);

    Optional<Item> getById(int id);
    List<Item> getItems(int ownerId);
    List<Item> findByText(String text);

}
