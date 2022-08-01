package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.config.PersistenceConfig;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.ItemRequestServiceImpl;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringJUnitConfig({PersistenceConfig.class, ItemRequestServiceImpl.class,
        UserServiceImpl.class, RequestRepository.class})
@DataJpaTest
public class IntegrationTestForItemRequest {

    private final TestEntityManager em;
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
