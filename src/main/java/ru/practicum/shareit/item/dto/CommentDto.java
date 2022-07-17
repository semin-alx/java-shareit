package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.common.controller.RestAction;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentDto {

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
    }

    @Null(groups = RestAction.Create.class)
    private Long id;

    @NotBlank(groups = {RestAction.Create.class, RestAction.Update.class})
    private String text;

    @Null(groups = {RestAction.Create.class, RestAction.Update.class})
    private LocalDateTime created;

    private CommentDto.User author;

    private CommentDto.Item item;

}
