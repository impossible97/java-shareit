package ru.practicum.shareit.validation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dao.UserStorage;
import ru.practicum.shareit.user.dto.UserDto;

@Component
@AllArgsConstructor
public class UserValidator {

    private final UserStorage userStorage;

    public void validateId(long userId) {
        if (!userStorage.getUsersIds().contains(userId)) {
            throw new NotFoundException("Пользователь с таким id = " + userId + " не найден!");
        }
    }

    public void validateEmail(UserDto user) {
        boolean b = userStorage.getAllUsers().stream().anyMatch(user1 -> user1.getEmail().equals(user.getEmail()));
        if (b) {
            throw new ConflictException("Пользователь с таким email = " + user.getEmail() + " уже существует!");
        }
    }

    public void validateEmailWithId(long userId, UserDto user) {
        boolean b = userStorage.getAllUsers().stream().anyMatch(user1 -> userId != user1.getId() &&
                user1.getEmail().equals(user.getEmail()));
        if (b) {
            throw new ConflictException("Пользователь с таким email = " + user.getEmail() + " уже существует!");
        }
    }
}
