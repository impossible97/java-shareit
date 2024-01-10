package ru.practicum.server.user.service;

import ru.practicum.server.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto getUser(long userId);

    List<UserDto> getAllUsers();

    UserDto createUser(UserDto user);

    UserDto updateUser(long userId, UserDto user);

    void deleteUser(long userId);
}
