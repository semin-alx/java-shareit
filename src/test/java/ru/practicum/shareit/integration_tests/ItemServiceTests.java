package ru.practicum.shareit.integration_tests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(locations = "classpath:application.properties")
public class ItemServiceTests {

    private final EntityManager em;
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

        Query q = em.createNativeQuery("UPDATE items SET available = TRUE");
        q.executeUpdate();

        List<ItemDto> list = itemService.findByText("ОТВЕРТКА");
        Assertions.assertEquals(2, list.size());
        Assertions.assertEquals("Отвертка крестовая", list.get(0).getName());
        Assertions.assertEquals("Отвертка плоская", list.get(1).getName());

    }

}
