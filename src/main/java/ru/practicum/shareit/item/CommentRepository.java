package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "SELECT comments.* FROM comments " +
                   "INNER JOIN items ON items.id = comments.item_id " +
                   "WHERE items.owner_id = :ownerId",
           nativeQuery = true)
    List<Comment> findByOwnerId(long ownerId);

    List<Comment> findByItemId(long itemId);

}
