package ru.practicum.shareit.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class CommentMapper {

    public Comment toEntity(CommentDto commentDto, User user, Item item) {
        Comment comment = new Comment();

        comment.setText(commentDto.getText());
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        comment.setItem(item);

        return comment;
    }

    public CommentDto toDto(Comment comment) {
        CommentDto commentDto = new CommentDto();

        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setAuthorName(comment.getAuthor().getName());
        commentDto.setCreated(comment.getCreated());

        return commentDto;
    }
}
