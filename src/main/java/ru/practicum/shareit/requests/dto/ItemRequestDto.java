package ru.practicum.shareit.requests.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.common.controller.RestAction;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ItemRequestDto {

    @Data
    @AllArgsConstructor
    public static class User {
        private int id;
        private String name;
    }

    @Null(groups = {RestAction.Create.class, RestAction.Update.class})
    private Integer id;

    @NotBlank
    private String description;

    @Null(groups = {RestAction.Create.class, RestAction.Update.class})
    private ItemRequestDto.User requester;

    @Null(groups = {RestAction.Create.class, RestAction.Update.class})
    private LocalDateTime created;

}
