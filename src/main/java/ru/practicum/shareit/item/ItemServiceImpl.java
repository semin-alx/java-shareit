package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.common.error_handling.exception.ItemAccessDeniedException;
import ru.practicum.shareit.common.error_handling.exception.ItemNotAvailableException;
import ru.practicum.shareit.common.error_handling.exception.ItemNotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;
    private final ItemRequestService requestService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    public ItemServiceImpl(ItemRepository itemRepository, UserService userService, ItemRequestService requestService, BookingRepository bookingRepository, CommentRepository commentRepository) {
        this.itemRepository = itemRepository;
        this.userService = userService;
        this.requestService = requestService;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public ItemDto create(long userId, ItemDto itemDto) {

        User owner = userService.checkAndGetUser(userId);

        ItemRequest request = null;

        if (itemDto.getRequestId() != null) {
            request = requestService.checkAndGetRequest(itemDto.getRequestId());
        }

        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(owner);
        item.setRequest(request);

        return ItemMapper.toItemDto(itemRepository.save(item));

    }

    @Override
    public ItemDto update(long ownerId, long itemId, ItemDto itemDto) {

        Item itemDB = checkAndGetItem(itemId);
        Item itemNew = ItemMapper.toItem(itemDto);

        if (itemDB.getOwner().getId() != ownerId) {
            throw new ItemAccessDeniedException("Доступ к чужим вещам запрещен");
        }

        if (itemNew.getName() != null) {
            itemDB.setName(itemDto.getName());
        }

        if (itemNew.getDescription() != null) {
            itemDB.setDescription(itemDto.getDescription());
        }

        if (itemNew.getAvailable() != null) {
            itemDB.setAvailable(itemNew.getAvailable());
        }

        return ItemMapper.toItemDto(itemRepository.save(itemDB));

    }

    @Override
    public void delete(long id) {
        Item item = checkAndGetItem(id);
        itemRepository.delete(item);
    }

    @Override
    public ItemDto getItemById(long id) {
        Item item = checkAndGetItem(id);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getItems(long ownerId) {
        userService.checkAndGetUser(ownerId);
        return itemRepository.findByOwnerId(ownerId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> findByText(String text) {
        if (!text.trim().isEmpty()) {
            return itemRepository.findAvailableByText(text).stream()
                    .map(ItemMapper::toItemDto)
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public CommentDto createComment(long authorId, long itemId, CommentDto commentDto) {

        User author = userService.checkAndGetUser(authorId);
        Item item = checkAndGetItem(itemId);

        Optional<Booking> booking = bookingRepository.findPastByBooker(authorId,
                        LocalDateTime.now()).stream()
                .filter(b -> b.getItem().getId() == itemId)
                .findFirst();

        if (!booking.isPresent()) {
            throw new ItemNotAvailableException("Отзыв возможен только после аренды");
        }

        Comment comment = new Comment(null, commentDto.getText(), item, author,
                LocalDateTime.now());

        return CommentMapper.toCommentDto(commentRepository.save(comment));

    }

    @Override
    public List<CommentDto> getCommentsByItem(long itemId) {
        return commentRepository.findByItemId(itemId).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> getCommentsByOwner(long ownerId) {
        return commentRepository.findByOwnerId(ownerId).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public Item checkAndGetItem(long id) {
        Optional<Item> item = itemRepository.findById(id);
        if (!item.isPresent()) {
            throw new ItemNotFoundException("Вещь с указанным идентификатором не найдена");
        } else {
            return item.get();
        }
    }

}
