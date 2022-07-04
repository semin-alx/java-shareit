package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemCreationDto;
import ru.practicum.shareit.item.dto.ItemEntityDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
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
    public ItemEntityDto create(@RequestHeader("X-Sharer-User-Id") int userId,
                                @Valid @RequestBody ItemCreationDto itemDto) {
        return itemService.create(userId, itemDto);
    }

    @PatchMapping(value = "/{id}")
    public ItemEntityDto update(@RequestHeader("X-Sharer-User-Id") int ownerId,
                                @PathVariable int id,
                                @Valid @RequestBody ItemUpdateDto itemDto) {
        return itemService.update(ownerId, id, itemDto);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable int id) {
        itemService.delete(id);
    }

    @GetMapping
    public List<ItemEntityDto> getItems(@RequestHeader("X-Sharer-User-Id") int ownerId) {
        return itemService.getItems(ownerId);
    }

    @GetMapping(value = "/{id}")
    public ItemEntityDto getItem(@PathVariable int id) {
        return itemService.getItemById(id);
    }

    @GetMapping(value = "/search")
    public List<ItemEntityDto> findByText(@RequestParam String text) {
        return itemService.findByText(text);
    }

}
