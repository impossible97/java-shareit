package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private Long generatedId = 0L;

    @Override
    public User getUser(long userId) {
        User user = users.get(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь с таким id = " + userId + " не найден!");
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User createUser(User user) {
        boolean b = new ArrayList<>(users.values()).stream().anyMatch(user1 -> user1.getEmail().equals(user.getEmail()));
        if (b) {
            throw new ConflictException("Пользователь с таким email = " + user.getEmail() + " уже существует!");
        }
        user.setId(++generatedId);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(long userId, User updatedUser) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Пользователь с таким id = " + userId + " не найден!");
        }
        boolean b = new ArrayList<>(users.values()).stream().anyMatch(user1 -> userId != user1.getId() &
                user1.getEmail().equals(updatedUser.getEmail()));
        if (b) {
            throw new ConflictException("Пользователь с таким email = " + updatedUser.getEmail() + " уже существует!");
        }
        User user = users.get(userId);
        if (updatedUser.getName() != null) {
            user.setName(updatedUser.getName());
        }
        if (updatedUser.getEmail() != null) {
            user.setEmail(updatedUser.getEmail());
        }
        users.put(userId, user);
        return user;
    }

    @Override
    public void deleteUser(long userId) {
        if (users.containsKey(userId)) {
            users.remove(userId);
        } else {
            throw new NotFoundException("Пользователь с таким id = " + userId + " не найден!");
        }
    }
}
