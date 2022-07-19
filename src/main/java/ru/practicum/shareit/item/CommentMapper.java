package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;

public class CommentMapper {

    public static CommentDto toCommentDto(Comment comment) {

        CommentDto.Item item = new CommentDto.Item(comment.getItem().getId(),
                                                   comment.getItem().getName());

        return new CommentDto(comment.getId(), comment.getText(),
                comment.getCreated(), comment.getAuthor().getName(), item);

    }

}
