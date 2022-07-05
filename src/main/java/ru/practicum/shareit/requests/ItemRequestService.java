package ru.practicum.shareit.requests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.error_handling.exception.ItemAccessDeniedException;
import ru.practicum.shareit.common.error_handling.exception.RequestNotFoundException;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.storage.ItemRequestStorage;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemRequestService {

    private final ItemRequestStorage itemRequestStorage;
    private final UserService userService;

    @Autowired
    public ItemRequestService(ItemRequestStorage itemRequestStorage, UserService userService) {
        this.itemRequestStorage = itemRequestStorage;
        this.userService = userService;
    }

    public ItemRequestDto create(int requesterId, ItemRequestDto requestDto) {

        User requester = userService.checkAndGetUser(requesterId);

        ItemRequest request = ItemRequestMapper.toItemRequest(requestDto);
        request.setRequester(requester);
        request.setCreated(LocalDateTime.now());

        request = itemRequestStorage.create(request);
        return ItemRequestMapper.toItemRequestEntityDto(request);

    }

    public ItemRequestDto update(int requesterId, int requestId,
                                 ItemRequestDto requestDto) {

        userService.checkAndGetUser(requesterId);
        ItemRequest requestDB = checkAndGetRequest(requestId);
        ItemRequest requestNew = ItemRequestMapper.toItemRequest(requestDto);

        if (requestDB.getRequester().getId() != requesterId) {
            throw new ItemAccessDeniedException("Нельзя изменять чужие запросы");
        }

        requestDB.setDescription(requestNew.getDescription());

        requestDB = itemRequestStorage.update(requestDB);
        return ItemRequestMapper.toItemRequestEntityDto(requestDB);

    }

    public void delete(int id) {
        checkAndGetRequest(id);
        itemRequestStorage.delete(id);
    }

    public ItemRequestDto getItemRequestById(int id) {
        ItemRequest request = checkAndGetRequest(id);
        return ItemRequestMapper.toItemRequestEntityDto(request);
    }

    public List<ItemRequestDto> getItemRequestsAll() {
        return itemRequestStorage.getItemRequestAll().stream()
                .map(ItemRequestMapper::toItemRequestEntityDto)
                .collect(Collectors.toList());
    }

    public ItemRequest checkAndGetRequest(int id) {
        Optional<ItemRequest> request = itemRequestStorage.getById(id);
        if (!request.isPresent()) {
            throw new RequestNotFoundException("Запрос по идентификатору не найден");
        } else {
            return request.get();
        }
    }

}
