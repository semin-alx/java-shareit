package ru.practicum.shareit.requests.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.common.memory_storage.MemoryBaseStorage;
import ru.practicum.shareit.requests.ItemRequest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MemoryItemRequestStorage extends MemoryBaseStorage implements ItemRequestStorage {

    @Override
    public ItemRequest create(ItemRequest itemRequest) {
        int newId = add(itemRequest);
        itemRequest.setId(newId);
        return itemRequest;
    }

    @Override
    public ItemRequest update(ItemRequest itemRequest) {
        getObjects().put(itemRequest.getId(), itemRequest);
        return itemRequest;
    }

    @Override
    public Optional<ItemRequest> getById(int id) {
        ItemRequest itemRequest = (ItemRequest)getObjects().get(id);

        if (itemRequest == null) {
            return Optional.empty();
        } else {
            return Optional.of(itemRequest);
        }
    }

    @Override
    public List<ItemRequest> getItemRequestAll() {
        return getObjects().values().stream()
                .map(o -> (ItemRequest)o)
                .collect(Collectors.toList());
    }
}
