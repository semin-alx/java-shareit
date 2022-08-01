package ru.practicum.shareit.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByOwnerIdOrderById(Long ownerId);

    Page<Item> findByOwnerIdOrderById(Long ownerId, Pageable pageable);

    @Query(value = "SELECT * FROM Items " +
                   "WHERE ((UPPER(name) LIKE CONCAT('%',UPPER(:text),'%')) " +
                   "OR (UPPER(description) LIKE CONCAT('%',UPPER(:text),'%'))) " +
                   "AND available",
            nativeQuery = true)
    List<Item> findAvailableByText(String text);

    @Query(value = "SELECT * FROM Items " +
            "WHERE ((UPPER(name) LIKE CONCAT('%',UPPER(:text),'%')) " +
            "OR (UPPER(description) LIKE CONCAT('%',UPPER(:text),'%'))) " +
            "AND available ORDER BY id /*:pageable*/",
            nativeQuery = true)
    List<Item> findAvailableByText(String text, Pageable pageable);

}
