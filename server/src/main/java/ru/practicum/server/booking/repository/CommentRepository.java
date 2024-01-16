package ru.practicum.server.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.server.item.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByAuthor_Id(long userId);

    List<Comment> findByItem_Owner_Id(long userId);

    List<Comment> findAllByItem_Id(long itemId);
}
