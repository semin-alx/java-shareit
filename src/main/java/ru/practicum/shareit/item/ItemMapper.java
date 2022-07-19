package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

public class ItemMapper {

    public static ItemDto toItemDto(Item item) {

        ItemDto.User owner = new ItemDto.User(item.getOwner().getId(),
                item.getOwner().getName());

        Long requestId = null;

        if (item.getRequest() != null) {
            requestId = item.getRequest().getId();
        }

        return new ItemDto(
                item.getId(), item.getName(), item.getDescription(), item.getAvailable(),
                owner, requestId, null, null, null);

    }

    // Здесь мы не можем полноценно преобразовать ItemDto в объект Item
    // Например, при обновлении клиент передает идентификатор владельца
    // через заголовок, здесь у нас этой информации нет.
    public static Item toItem(ItemDto itemDto) {
        return new Item(itemDto.getId(), itemDto.getName(), itemDto.getDescription(),
                itemDto.getAvailable(), null, null);
    }

}
