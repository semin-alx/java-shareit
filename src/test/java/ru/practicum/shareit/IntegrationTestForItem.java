package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.common.PersistenceConfig;
import ru.practicum.shareit.item.CommentRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.ItemRequestServiceImpl;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;

import javax.persistence.Query;
import java.util.List;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringJUnitConfig( {PersistenceConfig.class, ItemServiceImpl.class, UserServiceImpl.class,
        ItemRequestServiceImpl.class, BookingRepository.class, CommentRepository.class})
@DataJpaTest
public class IntegrationTestForItem {

    private final TestEntityManager em;
    private final ItemService itemService;
    private final UserService userService;

    @Test
    public void findByText() {

        UserDto userDto = userService.create(new UserDto(null, "user1",
                "email@aaa.ru"));

        ItemDto itemDto1 = new ItemDto(null,
                "Отвертка крестовая",
                "Почти новая", false, null, null, null, null,
                null);

        ItemDto itemDto2 = new ItemDto(null,
                "Отвертка плоская",
                "Почти новая", false, null, null, null, null,
                null);

        ItemDto itemDto3 = new ItemDto(null,
                "Пинцет",
                "Пинцет", false, null, null, null, null,
                null);

        itemService.create(userDto.getId(), itemDto1);
        itemService.create(userDto.getId(), itemDto2);
        itemService.create(userDto.getId(), itemDto3);

        Query q = em.getEntityManager().createNativeQuery("UPDATE items SET available = TRUE");
        q.executeUpdate();

        List<ItemDto> list = itemService.findByText("ОТВЕРТКА");
        Assertions.assertEquals(2, list.size());
        Assertions.assertEquals("Отвертка крестовая", list.get(0).getName());
        Assertions.assertEquals("Отвертка плоская", list.get(1).getName());

    }

}
