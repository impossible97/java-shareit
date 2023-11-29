package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto getUser(long userId);

    List<UserDto> getAllUsers();

    UserDto createUser(UserDto user);

    UserDto updateUser(long userId, UserDto user);

    void deleteUser(long userId);
}
