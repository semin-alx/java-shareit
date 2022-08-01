package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.BookingServiceImpl;
import ru.practicum.shareit.common.error_handling.exception.*;
import ru.practicum.shareit.item.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.ItemRequestServiceImpl;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.UserServiceImpl;
import java.util.ArrayList;
import java.util.Optional;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UnitTests {

    @Mock
    UserRepository mockUserRepository;

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

    @Mock
    ItemService mockItemService;

    @Mock
    RequestRepository mockRequestRepository;

    @Test
    void user_getUserById_not_found() {

        UserService userService = new UserServiceImpl(mockUserRepository);

        Mockito
                .when(mockUserRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        final UserNotFoundException exception = Assertions.assertThrows(
                UserNotFoundException.class,
                () -> userService.getUserById(1));

        Assertions.assertEquals("Пользователь с таким id не найден", exception.getMessage());
    }

    @Test
    void user_delete_not_found() {

        UserService userService = new UserServiceImpl(mockUserRepository);

        Mockito
                .when(mockUserRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        final UserNotFoundException exception = Assertions.assertThrows(
                UserNotFoundException.class,
                () -> userService.delete(1));

        Assertions.assertEquals("Пользователь с таким id не найден", exception.getMessage());
    }

    @Test
    void user_update_not_found() {

        UserService userService = new UserServiceImpl(mockUserRepository);

        Mockito
                .when(mockUserRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        final UserNotFoundException exception = Assertions.assertThrows(
                UserNotFoundException.class,
                () -> userService.update(1, null));

        Assertions.assertEquals("Пользователь с таким id не найден",
                exception.getMessage());
    }

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

    @Test
    void booking_checkAndGetBooking_not_found() {

        BookingService bookingService = new BookingServiceImpl(mockItemService, mockUserService,
                mockBookingRepository);

        Mockito
                .when(mockBookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        final ItemNotFoundException exception = Assertions.assertThrows(
                ItemNotFoundException.class,
                () -> bookingService.checkAndGetBooking(1));

        Assertions.assertEquals("Заказ по идентификатору не найден", exception.getMessage());
    }


    @Test
    void booking_changeStatus_access_people_booking() {

        BookingService bookingService = new BookingServiceImpl(mockItemService, mockUserService,
                mockBookingRepository);

        Booking booking = new Booking();
        booking.setItem(new Item());
        booking.getItem().setOwner(new User());
        booking.getItem().getOwner().setId(10L);
        booking.setBooker(new User());
        booking.getBooker().setId(20L);

        Mockito
                .when(mockBookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(booking));

        final ItemAccessDeniedException exception = Assertions.assertThrows(
                ItemAccessDeniedException.class,
                () -> bookingService.changeStatus(1, 1, true));

        Assertions.assertEquals("Доступ к чужим заказам запрещен", exception.getMessage());
    }

    @Test
    void booking_changeStatus_booker_approve() {

        BookingService bookingService = new BookingServiceImpl(mockItemService, mockUserService,
                mockBookingRepository);

        Booking booking = new Booking();
        booking.setItem(new Item());
        booking.getItem().setOwner(new User());
        booking.getItem().getOwner().setId(10L);
        booking.setBooker(new User());
        booking.getBooker().setId(1L);

        Mockito
                .when(mockBookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(booking));

        final WrongBookingStatusException exception = Assertions.assertThrows(
                WrongBookingStatusException.class,
                () -> bookingService.changeStatus(1, 1, true));

        Assertions.assertEquals("Заказчик не может подтверждать свой заказ", exception.getMessage());
    }

    @Test
    void booking_changeStatus_already_canceled() {

        BookingService bookingService = new BookingServiceImpl(mockItemService, mockUserService,
                mockBookingRepository);

        Booking booking = new Booking();
        booking.setItem(new Item());
        booking.getItem().setOwner(new User());
        booking.getItem().getOwner().setId(10L);
        booking.setBooker(new User());
        booking.getBooker().setId(1L);
        booking.setIsCanceled(true);

        Mockito
                .when(mockBookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(booking));

        final WrongBookingStatusException exception = Assertions.assertThrows(
                WrongBookingStatusException.class,
                () -> bookingService.changeStatus(10, 1, false));

        Assertions.assertEquals("Неверный статус заказа", exception.getMessage());
    }

    @Test
    void booking_changeStatus_already_aproved() {

        BookingService bookingService = new BookingServiceImpl(mockItemService, mockUserService,
                mockBookingRepository);

        Booking booking = new Booking();
        booking.setItem(new Item());
        booking.getItem().setOwner(new User());
        booking.getItem().getOwner().setId(10L);
        booking.setBooker(new User());
        booking.getBooker().setId(1L);
        booking.setIsCanceled(false);
        booking.setIsApproved(true);

        Mockito
                .when(mockBookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(booking));

        final BookingStatusAlreadySetException exception = Assertions.assertThrows(
                BookingStatusAlreadySetException.class,
                () -> bookingService.changeStatus(10, 1, true));

        Assertions.assertEquals("Неверный статус заказа", exception.getMessage());
    }

    @Test
    void booking_getBookingById_not_found() {

        BookingService bookingService = new BookingServiceImpl(mockItemService, mockUserService,
                mockBookingRepository);

        Mockito
                .when(mockBookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        final ItemNotFoundException exception = Assertions.assertThrows(
                ItemNotFoundException.class,
                () -> bookingService.getBookingById(1, 1));

        Assertions.assertEquals("Заказ по идентификатору не найден", exception.getMessage());
    }

    @Test
    void booking_changeStatus_people_booking() {

        BookingService bookingService = new BookingServiceImpl(mockItemService, mockUserService,
                mockBookingRepository);

        Booking booking = new Booking();
        booking.setItem(new Item());
        booking.getItem().setOwner(new User());
        booking.getItem().getOwner().setId(10L);
        booking.setBooker(new User());
        booking.getBooker().setId(20L);
        booking.setIsCanceled(false);
        booking.setIsApproved(true);

        Mockito
                .when(mockBookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(booking));

        final ItemAccessDeniedException exception = Assertions.assertThrows(
                ItemAccessDeniedException.class,
                () -> bookingService.getBookingById(1, 1));

        Assertions.assertEquals("Доступ к чужим заказам запрещен", exception.getMessage());
    }

    @Test
    void booking_delete_not_found() {

        BookingService bookingService = new BookingServiceImpl(mockItemService, mockUserService,
                mockBookingRepository);

        Mockito
                .when(mockBookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        final ItemNotFoundException exception = Assertions.assertThrows(
                ItemNotFoundException.class,
                () -> bookingService.delete(1));

        Assertions.assertEquals("Заказ по идентификатору не найден", exception.getMessage());
    }

}
