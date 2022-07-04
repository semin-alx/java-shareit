package ru.practicum.shareit.requests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemCreationDto;
import ru.practicum.shareit.item.dto.ItemEntityDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.requests.dto.ItemRequestCreationDto;
import ru.practicum.shareit.requests.dto.ItemRequestEntityDto;
import ru.practicum.shareit.requests.dto.ItemRequestUpdateDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @Autowired
    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @PostMapping
    public ItemRequestEntityDto create(@RequestHeader("X-Sharer-User-Id") int userId,
                                       @Valid @RequestBody ItemRequestCreationDto requestDto) {
        return itemRequestService.create(userId, requestDto);
    }

    @PatchMapping(value = "/{id}")
    public ItemRequestEntityDto update(@RequestHeader("X-Sharer-User-Id") int userId,
                                @PathVariable int id,
                                @Valid @RequestBody ItemRequestUpdateDto requestDto) {
        return itemRequestService.update(userId, id, requestDto);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable int id) {
        itemRequestService.delete(id);
    }

    @GetMapping
    public List<ItemRequestEntityDto> getItemRequestsAll() {
        return itemRequestService.getItemRequestsAll();
    }

    @GetMapping(value = "/{id}")
    public ItemRequestEntityDto getItem(@PathVariable int id) {
        return itemRequestService.getItemRequestById(id);
    }


}
