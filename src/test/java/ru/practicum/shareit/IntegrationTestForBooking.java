package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.config.PersistenceConfig;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequestServiceImpl;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringJUnitConfig({PersistenceConfig.class, BookingServiceImpl.class, ItemServiceImpl.class,
        UserServiceImpl.class, BookingRepository.class, ItemRequestServiceImpl.class})
@DataJpaTest
public class IntegrationTestForBooking {

    private final TestEntityManager em;
    private final BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService;

    @Test
    public void getBookingByBooker() {

        UserDto ownerDto = userService.create(new UserDto(null, "Хозяин",
                "owner@nnn.ru"));

        UserDto bookerDto = userService.create(new UserDto(null, "Заказчик",
                "booker@mmm.ru"));

        ItemDto itemDto1 = new ItemDto(null,
                "Отвертка крестовая1",
                "Почти новая", true, null, null,
                null, null, null);

        ItemDto itemDto2 = new ItemDto(null,
                "Отвертка крестовая2",
                "Почти новая", true, null, null,
                null, null, null);

        itemDto1 = itemService.create(ownerDto.getId(), itemDto1);
        itemDto2 = itemService.create(ownerDto.getId(), itemDto2);

        BookingDto booking1 = new BookingDto(null,
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(5),
                itemDto1.getId(), null, null, null);

        booking1 = bookingService.create(bookerDto.getId(), booking1);

        BookingDto booking2 = new BookingDto(null,
                LocalDateTime.now().plusDays(8), LocalDateTime.now().plusDays(10),
                itemDto2.getId(), null, null, null);

        booking2 = bookingService.create(bookerDto.getId(), booking2);

        bookingService.changeStatus(ownerDto.getId(), booking1.getId(), true);

        List<BookingDto> list = bookingService.getBookingByBooker(bookerDto.getId(),
                BookingFilterState.FUTURE);

        Assertions.assertEquals(2, list.size());
        Assertions.assertEquals(booking1.getId(), list.get(1).getId());
        Assertions.assertEquals(booking2.getId(), list.get(0).getId());
        Assertions.assertEquals(BookingStatus.APPROVED, list.get(1).getStatus());
        Assertions.assertEquals(BookingStatus.WAITING, list.get(0).getStatus());

    }

}
