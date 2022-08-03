package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import java.util.List;

public interface ItemService {

    ItemDto create(long userId, ItemDto itemDto);

    ItemDto update(long ownerId, long itemId, ItemDto itemDto);

    void delete(long id);

    ItemDto getItemById(long userId, long itemId);

    List<ItemDto> getItems(long ownerId);

    List<ItemDto> getItems(long ownerId, int from, int page);

    List<ItemDto> findByText(String text);

    List<ItemDto> findByText(String text, int from, int page);

    CommentDto createComment(long authorId, long itemId, CommentDto commentDto);

    List<CommentDto> getCommentsByItem(long itemId);

    List<CommentDto> getCommentsByOwner(long ownerId);

    Item checkAndGetItem(long id);

}
