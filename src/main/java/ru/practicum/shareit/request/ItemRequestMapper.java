package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class ItemRequestMapper {

    public static ItemRequestDto toItemRequestEntityDto(ItemRequest itemRequest) {

        ItemRequestDto.User user = new ItemRequestDto.User(
                itemRequest.getRequester().getId(),
                itemRequest.getRequester().getName());

        List<ItemRequestDto.Item> items = itemRequest.getItems().stream()
                .map(i -> new ItemRequestDto.Item(i.getId(),
                        i.getName(),
                        i.getDescription(),
                        i.getAvailable(),
                        i.getRequest().getId()))
                .collect(Collectors.toList());

        return new ItemRequestDto(itemRequest.getId(),
                itemRequest.getDescription(),
                user,
                itemRequest.getCreated(),
                items);

    }

    public static ItemRequest toItemRequest(ItemRequestDto requestDto) {
        return new ItemRequest(requestDto.getId(), requestDto.getDescription(),
                null, requestDto.getCreated(), new HashSet<>());
    }

}
