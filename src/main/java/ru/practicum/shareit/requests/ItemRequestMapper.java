package ru.practicum.shareit.requests;

import ru.practicum.shareit.requests.dto.ItemRequestDto;

public class ItemRequestMapper {

    public static ItemRequestDto toItemRequestEntityDto(ItemRequest itemRequest) {

        ItemRequestDto.User user = new ItemRequestDto.User(
                itemRequest.getRequester().getId(),
                itemRequest.getRequester().getName());

        return new ItemRequestDto(itemRequest.getId(),
                itemRequest.getDescription(),
                user,
                itemRequest.getCreated());

    }

    public static ItemRequest toItemRequest(ItemRequestDto requestDto) {
        return new ItemRequest(requestDto.getId(), requestDto.getDescription(),
                null, requestDto.getCreated());
    }

}
