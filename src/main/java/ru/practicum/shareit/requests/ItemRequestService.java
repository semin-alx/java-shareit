package ru.practicum.shareit.requests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.error_handling.exception.ItemAccessDeniedException;
import ru.practicum.shareit.common.error_handling.exception.RequestNotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemEntityDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.dto.ItemRequestCreationDto;
import ru.practicum.shareit.requests.dto.ItemRequestEntityDto;
import ru.practicum.shareit.requests.dto.ItemRequestUpdateDto;
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

    private final String ERR_REQUEST_BY_ID_NOT_FOUND = "Запрос по идентификатору не найден";
    private final String ERR_UPDATE_ACCESS_DENIED = "Нельзя изменять чужие запросы";

    @Autowired
    public ItemRequestService(ItemRequestStorage itemRequestStorage, UserService userService) {
        this.itemRequestStorage = itemRequestStorage;
        this.userService = userService;
    }

    public ItemRequestEntityDto create(int requesterId, ItemRequestCreationDto requestDto) {

        User requester = userService.checkAndGetUser(requesterId);

        ItemRequest request = new ItemRequest(null, requestDto.getDescription(), requester,
                LocalDateTime.now());

        request = itemRequestStorage.create(request);
        return ItemRequestMapper.toItemRequestEntityDto(request);

    }

    public ItemRequestEntityDto update(int requesterId, int requestId,
                                       ItemRequestUpdateDto requestDto) {

        User requester = userService.checkAndGetUser(requesterId);
        ItemRequest request = checkAndGetRequest(requestId);

        if (request.getRequester().getId() != requester.getId()) {
            throw new ItemAccessDeniedException(ERR_UPDATE_ACCESS_DENIED);
        }

        request.setDescription(requestDto.getDescription());

        request = itemRequestStorage.update(request);
        return ItemRequestMapper.toItemRequestEntityDto(request);

    }

    public void delete(int id) {
        checkAndGetRequest(id);
        itemRequestStorage.delete(id);
    }

    public ItemRequestEntityDto getItemRequestById(int id) {
        ItemRequest request = checkAndGetRequest(id);
        return ItemRequestMapper.toItemRequestEntityDto(request);
    }

    public List<ItemRequestEntityDto> getItemRequestsAll() {
        return itemRequestStorage.getItemRequestAll().stream()
                .map(ItemRequestMapper::toItemRequestEntityDto)
                .collect(Collectors.toList());
    }

    public ItemRequest checkAndGetRequest(int id) {
        Optional<ItemRequest> request = itemRequestStorage.getById(id);
        if (!request.isPresent()) {
            throw new RequestNotFoundException(ERR_REQUEST_BY_ID_NOT_FOUND);
        } else {
            return request.get();
        }
    }

}