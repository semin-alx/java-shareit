package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto create(long requesterId, ItemRequestDto requestDto);

    ItemRequestDto update(long requesterId, long requestId, ItemRequestDto requestDto);

    void delete(long id);

    ItemRequestDto getItemRequestByOwnerId(long ownerId, long id);

    List<ItemRequestDto> getItemsAll(long userId);

    List<ItemRequestDto> getItemsAllExceptUserId(long userId);

    List<ItemRequestDto> getItemsAllExceptUserId(long userId, int from, int page);

    ItemRequest checkAndGetRequest(long id);

    ItemRequest checkAndGetRequestByOwner(long ownerId, long id);

}
