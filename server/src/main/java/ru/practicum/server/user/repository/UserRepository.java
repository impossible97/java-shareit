package ru.practicum.server.user.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.model.UserProjection;

public interface UserRepository extends JpaRepository<User, Long> {

    UserProjection findUserById(Long userId);

    User findByEmail(String email);
}
