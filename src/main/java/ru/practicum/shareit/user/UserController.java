package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @GetMapping("{userId}")
    public User getUser(@PathVariable long userId) {
        return userService.getUser(userId);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping()
    public User createUser(@Validated @RequestBody User user) {
        return userService.createUser(user);
    }

    @PatchMapping("{userId}")
    public User updateUser(@RequestBody User user, @PathVariable long userId) {
        return userService.updateUser(userId, user);
    }

    @DeleteMapping("{userId}")
    public void deleteUser(@PathVariable long userId) {
        userService.deleteUser(userId);
    }
}
