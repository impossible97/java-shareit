package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dao.UserStorage;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;
    @Override
    public User getUser(long userId) {
        return userStorage.getUser(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @Override
    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    @Override
    public User updateUser(long userId, User user) {
        return userStorage.updateUser(userId, user);
    }

    @Override
    public void deleteUser(long userId) {
        userStorage.deleteUser(userId);
    }
}
