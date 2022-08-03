package ru.practicum.shareit.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<ItemRequest, Long> {

    // Получить все свои запросы
    List<ItemRequest> findByRequesterId(long requesterId);

    // Получить все запросы исключая свои
    List<ItemRequest> findAllByRequesterIdNot(long requesterId);

    // Получить все запросы исключая свои
    Page<ItemRequest> findAllByRequesterIdNot(long requesterId, Pageable pageable);

    // Получить свой request по id, чужие недоступны
    Optional<ItemRequest> findByRequesterIdAndId(long requestrId, long id);

    // Получить запрос по id
    Optional<ItemRequest> findById(long id);

}
