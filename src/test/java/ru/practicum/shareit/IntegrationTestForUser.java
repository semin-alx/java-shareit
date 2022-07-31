package ru.practicum.shareit;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.common.PersistenceConfig;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringJUnitConfig({PersistenceConfig.class, UserServiceImpl.class})
@DataJpaTest
public class IntegrationTestForUser {

    private final TestEntityManager em;
    private final UserService userService;

    @Test
    public void saveUser() {

        UserDto userDto = new UserDto(null, "user1", "user1@aaa.ru");
        userService.create(userDto);

        TypedQuery<User> query = em.getEntityManager()
                .createQuery("Select u from User u where u.email = :email", User.class);

        User user = query.setParameter("email", userDto.getEmail()).getSingleResult();
        Assertions.assertTrue(user != null);
        Assertions.assertTrue(user.getId() != null);
        Assertions.assertEquals(userDto.getName(), user.getName());

    }

    @Test
    public void getUsers() {

        UserDto userDto = new UserDto(null, "user1", "user1@aaa.ru");
        userService.create(userDto);

        List<UserDto> users = userService.getUsers();
        Assertions.assertTrue(users.size() == 1);
        Assertions.assertEquals(users.get(0).getName(), userDto.getName());

    }

}
