package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.controller.RestAction;
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
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") int userId,
                          @Valid @RequestBody ItemDto itemDto) {
        return itemService.create(userId, itemDto);
    }

    @PatchMapping(value = "/{id}")
    @Validated({RestAction.Update.class})
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") int ownerId,
                          @PathVariable int id,
                          @Valid @RequestBody ItemDto itemDto) {
        return itemService.update(ownerId, id, itemDto);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable int id) {
        itemService.delete(id);
    }

    @GetMapping
    public List<ItemDto> getItems(@RequestHeader("X-Sharer-User-Id") int ownerId) {
        return itemService.getItems(ownerId);
    }

    @GetMapping(value = "/{id}")
    public ItemDto getItem(@PathVariable int id) {
        return itemService.getItemById(id);
    }

    @GetMapping(value = "/search")
    public List<ItemDto> findByText(@RequestParam String text) {
        return itemService.findByText(text);
    }

}
