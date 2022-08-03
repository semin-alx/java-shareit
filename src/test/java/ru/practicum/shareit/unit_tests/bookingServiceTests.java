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
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.BookingServiceImpl;
import ru.practicum.shareit.common.error_handling.exception.BookingStatusAlreadySetException;
import ru.practicum.shareit.common.error_handling.exception.ItemAccessDeniedException;
import ru.practicum.shareit.common.error_handling.exception.ItemNotFoundException;
import ru.practicum.shareit.common.error_handling.exception.WrongBookingStatusException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.Optional;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class bookingServiceTests {

    @Mock
    BookingRepository mockBookingRepository;

    @Mock
    UserService mockUserService;

    @Mock
    ItemService mockItemService;

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
