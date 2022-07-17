package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.error_handling.exception.ItemAccessDeniedException;
import ru.practicum.shareit.common.error_handling.exception.RequestNotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;
import java.time.LocalDateTime;
import java.util.Optional;

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

        User requester = userService.checkAndGetUser(requesterId);

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
    public ItemRequestDto getItemRequestById(int id) {
        ItemRequest request = checkAndGetRequest(id);
        return ItemRequestMapper.toItemRequestEntityDto(request);
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

}
