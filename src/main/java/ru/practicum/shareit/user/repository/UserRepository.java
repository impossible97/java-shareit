package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserProjection;

public interface UserRepository extends JpaRepository<User, Long> {

    UserProjection findUserById(Long userId);
}
