package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemEntityDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {

    public static ItemEntityDto toItemEntityDto(Item item) {

        ItemEntityDto.User owner = new ItemEntityDto.User(item.getOwner().getId(),
                item.getOwner().getName());

        Integer requestId = null;
        if (item.getRequest().isPresent()) {
            requestId = item.getRequest().get().getId();
        }

        return new ItemEntityDto(
                item.getId(), item.getName(), item.getDescription(), item.getAvailable(),
                owner, requestId);

    }

}
