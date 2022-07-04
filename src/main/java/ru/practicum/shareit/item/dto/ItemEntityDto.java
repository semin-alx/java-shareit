package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Optional;

@Data
@AllArgsConstructor
public class ItemEntityDto {

    @Data
    @AllArgsConstructor
    public static class User {
        private int id;
        private String name;
    }

    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private ItemEntityDto.User owner;
    private Integer requestId;

}
