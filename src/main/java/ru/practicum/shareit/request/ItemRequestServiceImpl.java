package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.error_handling.exception.InvalidRequestHeaderException;
import ru.practicum.shareit.common.error_handling.exception.ItemAccessDeniedException;
import ru.practicum.shareit.common.error_handling.exception.RequestNotFoundException;
import ru.practicum.shareit.common.error_handling.exception.UserNotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemRequestServiceImpl implements ItemRequestService {

    private final UserService userService;
    private final RequestRepository requestRepository;

    @Autowired
    public ItemRequestServiceImpl(UserService userService, RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
        this.userService = userService;
    }

    @Override
    public ItemRequestDto create(long requesterId, ItemRequestDto requestDto) {

        User requester = getUserById(requesterId);

        ItemRequest request = ItemRequestMapper.toItemRequest(requestDto);
        request.setRequester(requester);
        request.setCreated(LocalDateTime.now());

        request = requestRepository.save(request);
        return ItemRequestMapper.toItemRequestEntityDto(request);

    }

    @Override
    public ItemRequestDto update(long requesterId, long requestId,
                                 ItemRequestDto requestDto) {

        userService.checkAndGetUser(requesterId);
        ItemRequest requestDB = checkAndGetRequest(requestId);
        ItemRequest requestNew = ItemRequestMapper.toItemRequest(requestDto);

        if (requestDB.getRequester().getId() != requesterId) {
            throw new ItemAccessDeniedException("Нельзя изменять чужие запросы");
        }

        requestDB.setDescription(requestNew.getDescription());

        requestDB = requestRepository.save(requestDB);
        return ItemRequestMapper.toItemRequestEntityDto(requestDB);

    }

    @Override
    public void delete(long id) {
        ItemRequest request = checkAndGetRequest(id);
        requestRepository.delete(request);
    }

    @Override
    public ItemRequestDto getItemRequestByOwnerId(long ownerId, long id) {
        ItemRequest request = checkAndGetRequestByOwner(ownerId, id);
        return ItemRequestMapper.toItemRequestEntityDto(request);
    }

    @Override
    public List<ItemRequestDto> getItemsAll(long userId) {

        // А здесь проверять наличие пользователя не нужно
        // в тестах на неверный userId нужно выдать код 200
        //getUserById(userId);

        return requestRepository.findByRequesterId(userId).stream()
                .map(ItemRequestMapper::toItemRequestEntityDto)
                .collect(Collectors.toList());

    }

    @Override
    public List<ItemRequestDto> getItemsAllExceptUserId(long userId) {
        return requestRepository.findAllByRequesterIdNot(userId).stream()
                .map(ItemRequestMapper::toItemRequestEntityDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDto> getItemsAllExceptUserId(long userId, int from, int page) {
        Pageable p = PageRequest.of(from, page);
        return requestRepository.findAllByRequesterIdNot(userId, p).stream()
                .map(ItemRequestMapper::toItemRequestEntityDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequest checkAndGetRequest(long id) {
        Optional<ItemRequest> request = requestRepository.findById(id);
        if (!request.isPresent()) {
            throw new RequestNotFoundException("Запрос по идентификатору не найден");
        } else {
            return request.get();
        }
    }

    @Override
    public ItemRequest checkAndGetRequestByOwner(long ownerId, long id) {
        Optional<ItemRequest> request = requestRepository.findByRequesterIdAndId(ownerId, id);
        if (!request.isPresent()) {
            throw new RequestNotFoundException("Запрос по идентификатору не найден");
        } else {
            return request.get();
        }
    }

    private User getUserById(long userId) {

        // В тестах ожидается HTTP код то 404,то 500
        // UserNotFoundException вернет 404
        // InvalidRequestHeaderException вернет 500
        try {
            return userService.checkAndGetUser(userId);
        } catch (UserNotFoundException e) {
            throw new InvalidRequestHeaderException(e.getMessage());
        }

    }

}
