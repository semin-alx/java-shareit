package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import java.util.List;

public interface ItemService {

    ItemDto create(long userId, ItemDto itemDto);

    ItemDto update(long ownerId, long itemId, ItemDto itemDto);

    void delete(long id);

    ItemDto getItemById(long id);

    List<ItemDto> getItems(long ownerId);

    List<ItemDto> findByText(String text);

    CommentDto createComment(long authorId, long itemId, CommentDto commentDto);

    List<CommentDto> getCommentsByItem(long itemId);

    List<CommentDto> getCommentsByOwner(long ownerId);

    Item checkAndGetItem(long id);

}
