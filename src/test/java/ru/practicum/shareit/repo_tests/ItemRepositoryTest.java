package ru.practicum.shareit.repo_tests;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;

import java.util.List;

@DataJpaTest
@TestPropertySource(locations = "classpath:application.properties")
public class ItemRepositoryTest {

    //@Autowired
    //TestEntityManager entityManager;

    @Autowired
    private ItemRepository repository;

    @BeforeAll
    public static void loadData(@Autowired TestEntityManager entityManager) {

        User user = new User(null, "user1", "aaa@bbb.ru");
        Item item1 = new Item(null, "Отвертка1", "", true, user, null);
        Item item2 = new Item(null, "Отвертка2", "", true, user, null);
        Item item3 = new Item(null, "Отвертка3", "", true, user, null);
        Item item4 = new Item(null, "Пинцет", "", true, user, null);

        entityManager.getEntityManager().persist(user);
        entityManager.getEntityManager().persist(item1);
        entityManager.getEntityManager().persist(item2);
        entityManager.getEntityManager().persist(item3);
        entityManager.getEntityManager().persist(item4);
        entityManager.flush();

    }

    @Test
    public void findAvailableByText1() {

        List<Item> list = repository.findAvailableByText("ОТВЕРТКА");
        Assertions.assertEquals(3, list.size());
    }

}
