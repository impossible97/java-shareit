package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    User getUser(long userId);

    List<User> getAllUsers();

    UserDto createUser(UserDto user);

    UserDto updateUser(long userId, UserDto user);

    void deleteUser(long userId);
}
