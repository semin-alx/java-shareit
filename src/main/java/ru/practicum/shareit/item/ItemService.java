package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.error_handling.exception.ItemAccessDeniedException;
import ru.practicum.shareit.common.error_handling.exception.ItemNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.requests.ItemRequestService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private final ItemStorage itemStorage;
    private final UserService userService;
    private final ItemRequestService requestService;

    @Autowired
    public ItemService(ItemStorage itemStorage, UserService userService, ItemRequestService requestService) {
        this.itemStorage = itemStorage;
        this.userService = userService;
        this.requestService = requestService;
    }

    public ItemDto create(int userId, ItemDto itemDto) {

        User owner = userService.checkAndGetUser(userId);

        Optional<ItemRequest> request;

        if (itemDto.getRequestId() != null) {
            ItemRequest r = requestService.checkAndGetRequest(itemDto.getRequestId());
            request = Optional.of(r);
        } else {
            request = Optional.empty();
        }

        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(owner);
        item.setRequest(request);

        return ItemMapper.toItemDto(itemStorage.create(item));

    }

    public ItemDto update(int ownerId, int itemId, ItemDto itemDto) {

        Item itemDB = checkAndGetItem(itemId);
        Item itemNew = ItemMapper.toItem(itemDto);

        if (itemDB.getOwner().getId() != ownerId) {
            throw new ItemAccessDeniedException("Доступ к чужим вещам запрещен");
        }

        if (itemNew.getName() != null) {
            itemDB.setName(itemDto.getName());
        }

        if (itemNew.getDescription() != null) {
            itemDB.setDescription(itemDto.getDescription());
        }

        if (itemNew.getAvailable() != null) {
            itemDB.setAvailable(itemNew.getAvailable());
        }

        return ItemMapper.toItemDto(itemStorage.update(itemDB));

    }

    public void delete(int id) {
        checkAndGetItem(id);
        itemStorage.delete(id);
    }

    public ItemDto getItemById(int id) {
        Item item = checkAndGetItem(id);
        return ItemMapper.toItemDto(item);
    }

    public List<ItemDto> getItems(int ownerId) {
        userService.checkAndGetUser(ownerId);
        return itemStorage.getItems(ownerId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    public List<ItemDto> findByText(String text) {
        if (!text.trim().isEmpty()) {
            return itemStorage.findByText(text).stream()
                    .filter(Item::getAvailable)
                    .map(ItemMapper::toItemDto)
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    public Item checkAndGetItem(int id) {
        Optional<Item> item = itemStorage.getById(id);
        if (!item.isPresent()) {
            throw new ItemNotFoundException("Вещь с указанным идентификатором не найдена");
        } else {
            return item.get();
        }
    }

}
