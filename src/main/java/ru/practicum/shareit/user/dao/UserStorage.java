package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage {

    User getUser(long userId);

    List<User> getAllUsers();

    User createUser(User user);

    User updateUser(long userId, User user);

    Set<Long> getUsersIds();

    void deleteUser(long userId);
}
