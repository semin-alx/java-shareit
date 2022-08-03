package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.common.controller.RestAction;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ItemRequestDto {

    @Data
    @AllArgsConstructor
    public static class User {
        private Long id;
        private String name;
    }

    @Data
    @AllArgsConstructor
    public static class Item {
        private Long id;
        private String name;
        private String description;
        private Boolean available;
        private Long requestId;
    }

    @Null(groups = {RestAction.Create.class, RestAction.Update.class})
    private Long id;

    @NotBlank
    private String description;

    @Null(groups = {RestAction.Create.class, RestAction.Update.class})
    private ItemRequestDto.User requester;

    @Null(groups = {RestAction.Create.class, RestAction.Update.class})
    private LocalDateTime created;

    @Null(groups = {RestAction.Create.class, RestAction.Update.class})
    private List<Item> items;

    public ItemRequestDto() {

    }

}
