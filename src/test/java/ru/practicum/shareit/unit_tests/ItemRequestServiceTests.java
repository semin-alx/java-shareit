package ru.practicum.shareit.unit_tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.common.error_handling.exception.ItemAccessDeniedException;
import ru.practicum.shareit.common.error_handling.exception.RequestNotFoundException;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.ItemRequestServiceImpl;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.Optional;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ItemRequestServiceTests {

    @Mock
    UserService mockUserService;

    @Mock
    RequestRepository mockRequestRepository;


    @Test
    void request_checkAndGetRequestByOwner_not_found() {

        ItemRequestService itemRequestService = new ItemRequestServiceImpl(mockUserService,
                mockRequestRepository);

        Mockito
                .when(mockRequestRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        final RequestNotFoundException exception = Assertions.assertThrows(
                RequestNotFoundException.class,
                () -> itemRequestService.checkAndGetRequestByOwner(1, 1));

        Assertions.assertEquals("Запрос по идентификатору не найден", exception.getMessage());
    }

    @Test
    void request_checkAndGetRequest_not_found() {

        ItemRequestService itemRequestService = new ItemRequestServiceImpl(mockUserService,
                mockRequestRepository);

        Mockito
                .when(mockRequestRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        final RequestNotFoundException exception = Assertions.assertThrows(
                RequestNotFoundException.class,
                () -> itemRequestService.checkAndGetRequest(1));

        Assertions.assertEquals("Запрос по идентификатору не найден", exception.getMessage());
    }

    @Test
    void request_change_people_requests() {

        ItemRequestService itemRequestService = new ItemRequestServiceImpl(mockUserService,
                mockRequestRepository);

        ItemRequest request = new ItemRequest();
        request.setRequester(new User());
        request.getRequester().setId(10L);

        ItemRequestDto requestDto = new ItemRequestDto(null, null, null,
                null, null);

        Mockito
                .when(mockRequestRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(request));

        final ItemAccessDeniedException exception = Assertions.assertThrows(
                ItemAccessDeniedException.class,
                () -> itemRequestService.update(1, 1, requestDto));

        Assertions.assertEquals("Нельзя изменять чужие запросы", exception.getMessage());
    }

}
