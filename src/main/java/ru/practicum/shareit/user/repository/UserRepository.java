package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserShort;

public interface UserRepository extends JpaRepository<User, Long> {

    UserShort findUserById(Long userId);
}
