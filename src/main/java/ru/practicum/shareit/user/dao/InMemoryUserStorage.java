package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private Long generatedId = 0L;

    @Override
    public User getUser(long userId) {
        return users.get(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User createUser(User user) {
        user.setId(++generatedId);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(long userId, User updatedUser) {
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
    public Set<Long> getUsersIds() {
        return new HashSet<>(users.keySet());
    }

    @Override
    public void deleteUser(long userId) {
            users.remove(userId);
    }
}
