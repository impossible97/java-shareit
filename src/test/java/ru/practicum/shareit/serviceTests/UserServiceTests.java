package ru.practicum.shareit.serviceTests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Mock
    UserMapper userMapper;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Test
    void testCreateUser() {


    }
}
