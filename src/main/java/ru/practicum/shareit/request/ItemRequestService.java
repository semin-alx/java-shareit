package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

public interface ItemRequestService {

    ItemRequestDto create(long requesterId, ItemRequestDto requestDto);

    ItemRequestDto update(long requesterId, long requestId, ItemRequestDto requestDto);

    void delete(long id);

    ItemRequestDto getItemRequestById(int id);

    ItemRequest checkAndGetRequest(long id);

}
