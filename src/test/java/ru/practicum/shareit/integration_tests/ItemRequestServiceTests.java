package ru.practicum.shareit.integration_tests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(locations = "classpath:application.properties")
public class ItemRequestServiceTests {

    private final ItemRequestService itemRequestService;
    private final UserService userService;

    @Test
    public void getItemsAll() {

        UserDto userDto = userService.create(new UserDto(null, "user1",
                "email@aaa.ru"));

        ItemRequestDto request1 = new ItemRequestDto(null, "Отвертка плоская",
                null, null, null);

        ItemRequestDto request2 = new ItemRequestDto(null, "Отвертка крестовая",
                null, null, null);

        itemRequestService.create(userDto.getId(), request1);
        itemRequestService.create(userDto.getId(), request2);

        List<ItemRequestDto> list = itemRequestService.getItemsAll(userDto.getId());
        Assertions.assertEquals(2, list.size());

    }


}
