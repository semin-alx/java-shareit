package ru.practicum.shareit.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.user.User;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ItemRequest {
    private Integer id;
    private String description;
    private User requester;
    private LocalDateTime created;
}
