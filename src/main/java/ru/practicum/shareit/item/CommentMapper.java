package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;

public class CommentMapper {

    public static CommentDto toCommentDto(Comment comment) {

        CommentDto.Item item = new CommentDto.Item(comment.getItem().getId(),
                                                   comment.getItem().getName());

        CommentDto.User user = new CommentDto.User(comment.getAuthor().getId(),
                                                   comment.getAuthor().getName());

        return new CommentDto(comment.getId(), comment.getText(),
                comment.getCreated(), user, item);

    }

}
