package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    User getUser(long userId);

    List<User> getAllUsers();

    User createUser(User user);

    User updateUser(long userId, User user);

    void deleteUser(long userId);
}
