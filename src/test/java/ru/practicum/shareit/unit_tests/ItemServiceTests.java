package ru.practicum.shareit.unit_tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.common.error_handling.exception.ItemAccessDeniedException;
import ru.practicum.shareit.common.error_handling.exception.ItemNotAvailableException;
import ru.practicum.shareit.common.error_handling.exception.ItemNotFoundException;
import ru.practicum.shareit.common.error_handling.exception.UserNotFoundException;
import ru.practicum.shareit.item.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.Optional;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ItemServiceTests {

    @Mock
    ItemRepository mockItemRepository;

    @Mock
    ItemRequestService mockItemRequestService;

    @Mock
    BookingRepository mockBookingRepository;

    @Mock
    CommentRepository mockCommentRepository;

    @Mock
    UserService mockUserService;

    @Test
    void item_checkAndGetItem_not_found() {

        ItemService itemService = new ItemServiceImpl(mockItemRepository, mockUserService,
                mockItemRequestService, mockBookingRepository, mockCommentRepository);

        Mockito
                .when(mockItemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        final ItemNotFoundException exception = Assertions.assertThrows(
                ItemNotFoundException.class,
                () -> itemService.checkAndGetItem(1));

        Assertions.assertEquals("Вещь с указанным идентификатором не найдена",
                exception.getMessage());

    }

    @Test
    void item_createComment_user_not_found() {

        ItemService itemService = new ItemServiceImpl(mockItemRepository, mockUserService,
                mockItemRequestService, mockBookingRepository, mockCommentRepository);

        Mockito
                .when(mockUserService.checkAndGetUser(Mockito.anyLong()))
                .thenThrow(new UserNotFoundException("Пользователь с таким id не найден"));

        final UserNotFoundException exception = Assertions.assertThrows(
                UserNotFoundException.class,
                () -> itemService.createComment(1, 1, null));

        Assertions.assertEquals("Пользователь с таким id не найден",
                exception.getMessage());

    }

    @Test
    void item_createComment_item_not_found() {

        ItemService itemService = new ItemServiceImpl(mockItemRepository, mockUserService,
                mockItemRequestService, mockBookingRepository, mockCommentRepository);

        Mockito
                .when(mockUserService.checkAndGetUser(Mockito.anyLong()))
                .thenReturn(new User());

        Mockito
                .when(mockItemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        final ItemNotFoundException exception = Assertions.assertThrows(
                ItemNotFoundException.class,
                () -> itemService.createComment(1, 1, null));

        Assertions.assertEquals("Вещь с указанным идентификатором не найдена",
                exception.getMessage());

    }

    @Test
    void item_createComment_during_rent() {

        ItemService itemService = new ItemServiceImpl(mockItemRepository, mockUserService,
                mockItemRequestService, mockBookingRepository, mockCommentRepository);

        Mockito
                .when(mockUserService.checkAndGetUser(Mockito.anyLong()))
                .thenReturn(new User());

        Mockito
                .when(mockItemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(new Item()));

        Mockito
                .when(mockBookingRepository.findPastByBooker(Mockito.anyLong(), Mockito.any()))
                .thenReturn(new ArrayList<Booking>());

        final ItemNotAvailableException exception = Assertions.assertThrows(
                ItemNotAvailableException.class,
                () -> itemService.createComment(1, 1, null));

        Assertions.assertEquals("Отзыв возможен только после аренды",
                exception.getMessage());

    }

    @Test
    void item_getItems_user_not_found() {

        ItemService itemService = new ItemServiceImpl(mockItemRepository, mockUserService,
                mockItemRequestService, mockBookingRepository, mockCommentRepository);

        Mockito
                .when(mockUserService.checkAndGetUser(Mockito.anyLong()))
                .thenThrow(new UserNotFoundException("Пользователь с таким id не найден"));

        final UserNotFoundException exception = Assertions.assertThrows(
                UserNotFoundException.class,
                () -> itemService.getItems(1));

        Assertions.assertEquals("Пользователь с таким id не найден",
                exception.getMessage());

    }

    @Test
    void item_getItemById_item_not_found() {

        ItemService itemService = new ItemServiceImpl(mockItemRepository, mockUserService,
                mockItemRequestService, mockBookingRepository, mockCommentRepository);

        Mockito
                .when(mockItemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        final ItemNotFoundException exception = Assertions.assertThrows(
                ItemNotFoundException.class,
                () -> itemService.getItemById(1, 1));

        Assertions.assertEquals("Вещь с указанным идентификатором не найдена",
                exception.getMessage());

    }

    @Test
    void item_delete_item_not_found() {

        ItemService itemService = new ItemServiceImpl(mockItemRepository, mockUserService,
                mockItemRequestService, mockBookingRepository, mockCommentRepository);

        Mockito
                .when(mockItemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        final ItemNotFoundException exception = Assertions.assertThrows(
                ItemNotFoundException.class,
                () -> itemService.delete(1));

        Assertions.assertEquals("Вещь с указанным идентификатором не найдена",
                exception.getMessage());

    }

    @Test
    void item_update_item_not_found() {

        ItemService itemService = new ItemServiceImpl(mockItemRepository, mockUserService,
                mockItemRequestService, mockBookingRepository, mockCommentRepository);

        Mockito
                .when(mockItemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        final ItemNotFoundException exception = Assertions.assertThrows(
                ItemNotFoundException.class,
                () -> itemService.update(1, 1, null));

        Assertions.assertEquals("Вещь с указанным идентификатором не найдена",
                exception.getMessage());

    }

    @Test
    void item_update_people_things() {

        ItemService itemService = new ItemServiceImpl(mockItemRepository, mockUserService,
                mockItemRequestService, mockBookingRepository, mockCommentRepository);

        Item testItem = new Item();
        User testOwner = new User();
        testOwner.setId(10L);
        testItem.setOwner(testOwner);

        ItemDto testItemtemDto = new ItemDto(null, null, "",
                true, null, null, null, null,
                null);

        Mockito
                .when(mockItemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(testItem));

        final ItemAccessDeniedException exception = Assertions.assertThrows(
                ItemAccessDeniedException.class,
                () -> itemService.update(1, 1, testItemtemDto));

        Assertions.assertEquals("Доступ к чужим вещам запрещен",
                exception.getMessage());

    }

    @Test
    void item_create_user_not_found() {

        ItemService itemService = new ItemServiceImpl(mockItemRepository, mockUserService,
                mockItemRequestService, mockBookingRepository, mockCommentRepository);

        Mockito
                .when(mockUserService.checkAndGetUser(Mockito.anyLong()))
                .thenThrow(new UserNotFoundException("Пользователь с таким id не найден"));

        final UserNotFoundException exception = Assertions.assertThrows(
                UserNotFoundException.class,
                () -> itemService.create(1, null));

        Assertions.assertEquals("Пользователь с таким id не найден",
                exception.getMessage());

    }


}
