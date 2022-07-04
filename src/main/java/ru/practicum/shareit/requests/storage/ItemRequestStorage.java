package ru.practicum.shareit.requests.storage;

import ru.practicum.shareit.requests.ItemRequest;

import java.util.List;
import java.util.Optional;

public interface ItemRequestStorage {

    ItemRequest create(ItemRequest itemRequest);
    ItemRequest update(ItemRequest itemRequest);
    void delete(int id);

    Optional<ItemRequest> getById(int id);
    List<ItemRequest> getItemRequestAll();

}
