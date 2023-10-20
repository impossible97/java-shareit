package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.mapper.UserMapper;
import ru.practicum.shareit.user.dao.UserStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.validation.UserValidator;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;
    private final UserMapper userMapper;
    private final UserValidator validator;

    @Override
    public User getUser(long userId) {
        validator.validateId(userId);
        return userStorage.getUser(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        validator.validateEmail(userDto);
        return userMapper.toDto(userStorage.createUser(userMapper.toEntity(userDto)));
    }

    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        validator.validateId(userId);
        validator.validateEmailWithId(userId, userDto);
        return userMapper.toDto(userStorage.updateUser(userId, userMapper.toEntity(userDto)));
    }

    @Override
    public void deleteUser(long userId) {
        validator.validateId(userId);
        userStorage.deleteUser(userId);
    }
}