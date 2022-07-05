package ru.practicum.shareit.requests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.controller.RestAction;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @Autowired
    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @PostMapping
    @Validated({RestAction.Create.class})
    public ItemRequestDto create(@RequestHeader("X-Sharer-User-Id") int userId,
                                 @Valid @RequestBody ItemRequestDto requestDto) {
        return itemRequestService.create(userId, requestDto);
    }

    @PatchMapping(value = "/{id}")
    @Validated({RestAction.Update.class})
    public ItemRequestDto update(@RequestHeader("X-Sharer-User-Id") int userId,
                                 @PathVariable int id,
                                 @Valid @RequestBody ItemRequestDto requestDto) {
        return itemRequestService.update(userId, id, requestDto);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable int id) {
        itemRequestService.delete(id);
    }

    @GetMapping
    public List<ItemRequestDto> getItemRequestsAll() {
        return itemRequestService.getItemRequestsAll();
    }

    @GetMapping(value = "/{id}")
    public ItemRequestDto getItem(@PathVariable int id) {
        return itemRequestService.getItemRequestById(id);
    }


}
