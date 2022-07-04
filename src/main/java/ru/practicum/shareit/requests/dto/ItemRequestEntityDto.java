package ru.practicum.shareit.requests.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ItemRequestEntityDto {

    @Data
    @AllArgsConstructor
    public static class User {
        private int id;
        private String name;
    }

    private int id;
    private String description;
    private ItemRequestEntityDto.User requester;
    private LocalDateTime created;

}
