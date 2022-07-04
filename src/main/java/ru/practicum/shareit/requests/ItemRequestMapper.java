package ru.practicum.shareit.requests;

import ru.practicum.shareit.requests.dto.ItemRequestEntityDto;

public class ItemRequestMapper {

    public static ItemRequestEntityDto toItemRequestEntityDto(ItemRequest itemRequest) {

        ItemRequestEntityDto.User user = new ItemRequestEntityDto.User(
                itemRequest.getRequester().getId(),
                itemRequest.getRequester().getName());

        return new ItemRequestEntityDto(itemRequest.getId(),
                itemRequest.getDescription(),
                user,
                itemRequest.getCreated());

    }

}
