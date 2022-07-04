package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.common.memory_storage.MemoryBaseStorage;
import ru.practicum.shareit.item.model.Item;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MemoryItemStorage extends MemoryBaseStorage implements ItemStorage {

    @Override
    public Item create(Item item) {
        int newId = add(item);
        item.setId(newId);
        return item;
    }

    @Override
    public Item update(Item item) {
        getObjects().put(item.getId(), item);
        return item;
    }

    @Override
    public Optional<Item> getById(int id) {

        Item item = (Item)getObjects().get(id);

        if (item == null) {
            return Optional.empty();
        } else {
            return Optional.of(item);
        }

    }

    @Override
    public List<Item> getItems(int ownerId) {
        return getObjects().values().stream()
                .map(o -> (Item)o)
                .filter(i -> i.getOwner().getId() == ownerId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> findByText(String text) {

        String lowCaseText = text.toLowerCase();

        return getObjects().values().stream()
                .map(o -> (Item)o)
                .filter(i -> ((i.getName().toLowerCase().contains(lowCaseText)) ||
                             (i.getDescription().toLowerCase().contains(lowCaseText))))
                .collect(Collectors.toList());

    }

}
