package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.controller.RestAction;
import ru.practicum.shareit.common.error_handling.exception.InvalidRequestParamException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
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
    public ItemRequestDto create(@RequestHeader("X-Sharer-User-Id") long userId,
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

    @GetMapping(value = "/{id}")
    public ItemRequestDto getItem(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                  @PathVariable long id) {
        return itemRequestService.getItemRequestByOwnerId(ownerId, id);
    }

    @GetMapping
    public List<ItemRequestDto> getItemsByUser(@RequestHeader("X-Sharer-User-Id") long userId) {
       return itemRequestService.getItemsAll(userId);
    }

    // /requests/all?from=0&page=0
    @GetMapping(value = "/all")
    public List<ItemRequestDto> getItemsAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                            @RequestParam(required = false) Integer from,
                                            @RequestParam(required = false) Integer page) {

        if ((from == null) && (page == null)) {
            return itemRequestService.getItemsAllExceptUserId(userId);
        } else if ((from != null) && (page != null) && (from >= 0) && (page > 0)) {
            return itemRequestService.getItemsAllExceptUserId(userId, from, page);
        } else {
            throw new InvalidRequestParamException("Неверные параметры from или page");
        }

    }

}
