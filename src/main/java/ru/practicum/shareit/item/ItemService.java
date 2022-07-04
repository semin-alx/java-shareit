package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.error_handling.exception.ItemAccessDeniedException;
import ru.practicum.shareit.common.error_handling.exception.ItemNotFoundException;
import ru.practicum.shareit.item.dto.ItemCreationDto;
import ru.practicum.shareit.item.dto.ItemEntityDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.requests.ItemRequestService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.storage.UserStorage;

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
    public ItemService(ItemStorage itemStorage, UserService userService, UserStorage userStorage, ItemRequestService requestService) {
        this.itemStorage = itemStorage;
        this.userService = userService;
        this.requestService = requestService;
    }

    public ItemEntityDto create(int userId, ItemCreationDto itemDto) {

        User owner = userService.checkAndGetUser(userId);

        Optional<ItemRequest> request;

        if (itemDto.getRequestId() != null) {
            ItemRequest r = requestService.checkAndGetRequest(itemDto.getRequestId());
            request = Optional.of(r);
        } else {
            request = Optional.empty();
        }

        Item item = new Item(null, itemDto.getName(), itemDto.getDescription(),
                itemDto.getAvailable(), owner, request);

        return ItemMapper.toItemEntityDto(itemStorage.create(item));

    }

    public ItemEntityDto update(int ownerId, int itemId, ItemUpdateDto itemDto) {

        Item item0 = checkAndGetItem(itemId);

        if (item0.getOwner().getId() != ownerId) {
            throw new ItemAccessDeniedException("Доступ к чужим вещам запрещен");
        }

        if (itemDto.getName() != null) {
            item0.setName(itemDto.getName());
        }

        if (itemDto.getDescription() != null) {
            item0.setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            item0.setAvailable(itemDto.getAvailable());
        }

        return ItemMapper.toItemEntityDto(itemStorage.update(item0));

    }

    public void delete(int id) {
        checkAndGetItem(id);
        itemStorage.delete(id);
    }

    public ItemEntityDto getItemById(int id) {
        Item item = checkAndGetItem(id);
        return ItemMapper.toItemEntityDto(item);
    }

    public List<ItemEntityDto> getItems(int ownerId) {
        userService.checkAndGetUser(ownerId);
        return itemStorage.getItems(ownerId).stream()
                .map(ItemMapper::toItemEntityDto)
                .collect(Collectors.toList());
    }

    public List<ItemEntityDto> findByText(String text) {
        if (!text.trim().isEmpty()) {
            return itemStorage.findByText(text).stream()
                    .filter(Item::getAvailable)
                    .map(ItemMapper::toItemEntityDto)
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
