package ru.practicum.server.mapperTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.server.item.dto.CommentDto;
import ru.practicum.server.item.model.Comment;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.mapper.CommentMapper;
import ru.practicum.server.user.model.User;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

class CommentMapperTest {

    private CommentDto commentDto;
    private User user;
    private Item item;
    private Comment comment;
    private static final CommentMapper commentMapper = new CommentMapper();

    @BeforeEach
    void setObjects() {
        user = new User();
        user.setId(1L);
        user.setName("Name");
        user.setEmail("email@email.com");

        item = new Item();
        item.setId(1L);
        item.setName("Item");
        item.setDescription("Description");
        item.setAvailable(true);

        commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setText("Text");
        commentDto.setCreated(LocalDateTime.now());
        commentDto.setAuthorName(user.getName());

        comment = new Comment();

        comment.setId(commentDto.getId());
        comment.setText(commentDto.getText());
        comment.setCreated(commentDto.getCreated());
        comment.setItem(item);
        comment.setAuthor(user);
    }

    @Test
    void toDtoTest() {
        CommentDto dto = commentMapper.toDto(comment);

        assertThat(dto, notNullValue());
        assertThat(dto, equalTo(commentDto));
    }

    @Test
    void toEntityTest() {
        Comment entity = commentMapper.toEntity(commentDto, user, item);

        assertThat(entity, notNullValue());
        assertThat(entity.getText(), equalTo(comment.getText()));
        assertThat(entity.getItem(), equalTo(comment.getItem()));
        assertThat(entity.getAuthor(), equalTo(comment.getAuthor()));
    }
}
