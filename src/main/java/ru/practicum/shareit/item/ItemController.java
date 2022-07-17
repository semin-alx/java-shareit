package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.controller.RestAction;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@Validated
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    @Validated({RestAction.Create.class})
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") long userId,
                          @Valid @RequestBody ItemDto itemDto) {
        return itemService.create(userId, itemDto);
    }

    @PatchMapping(value = "/{id}")
    @Validated({RestAction.Update.class})
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") long ownerId,
                          @PathVariable long id,
                          @Valid @RequestBody ItemDto itemDto) {
        return itemService.update(ownerId, id, itemDto);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable long id) {
        itemService.delete(id);
    }

    @GetMapping
    public List<ItemDto> getItems(@RequestHeader("X-Sharer-User-Id") long ownerId) {
        return itemService.getItems(ownerId);
    }

    @GetMapping(value = "/{id}")
    public ItemDto getItem(@PathVariable long id) {
        return itemService.getItemById(id);
    }

    @GetMapping(value = "/search")
    public List<ItemDto> findByText(@RequestParam String text) {
        return itemService.findByText(text);
    }

    @PostMapping(value = "/{itemId}/comment")
    public CommentDto create(@RequestHeader("X-Sharer-User-Id") long authorId,
                             @PathVariable long itemId,
                             @Valid @RequestBody CommentDto commentDto) {
        return itemService.createComment(authorId, itemId, commentDto);
    }

    // В ТЗ просмотр отзывов идет по GET /items/{itemId}
    // но этот эндпойнт уже используется для получения информации о вещи
    // тестов для прояснения картины пока нет, будем использовать
    // эндпойнт GET /items/{itemId}/comment
    @GetMapping(value = "/{itemId}/comments")
    public List<CommentDto> getCommentsByItem(@PathVariable long itemId) {
        return itemService.getCommentsByItem(itemId);
    }

    @GetMapping(value = "/comments")
    public List<CommentDto> getCommentsByOwner(@RequestHeader("X-Sharer-User-Id") long ownerId) {
        return itemService.getCommentsByOwner(ownerId);
    }

}
