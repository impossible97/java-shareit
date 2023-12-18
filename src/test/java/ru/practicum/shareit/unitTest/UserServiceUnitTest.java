package ru.practicum.shareit.unitTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.mapper.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {

    @Mock
    UserRepository userRepository;
    @Mock
    UserMapper userMapper;
    @InjectMocks
    UserServiceImpl userService;

    @Test
    void getUserNotFoundTest() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenThrow(new NotFoundException("Пользователь на найден"));

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> userService.getUser(anyLong()));
        assertThat("Пользователь на найден", equalTo(exception.getMessage()));
        Mockito.verify(userRepository, Mockito.times(1)).findById(anyLong());
        Mockito.verify(userMapper, Mockito.times(0)).toDto(any());
    }

    @Test
    void updateNameThenEmailWithMockTest() {
        long userId = 1L;

        UserDto userDto = new UserDto();
        userDto.setId(userId);

        User user = new User();
        user.setId(userId);

        Mockito
                .when(userRepository.findById(userDto.getId()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(userRepository.save(user))
                .thenReturn(user);
        Mockito
                .when(userMapper.toDto(user))
                .thenReturn(userDto);
        Mockito
                .when(userMapper.toEntity(userDto))
                .thenReturn(user);

        UserDto createdUser = userService.createUser(userDto);

        createdUser.setName("updatedUser");
        UserDto updatedUser = userService.updateUser(createdUser.getId(), createdUser);

        assertThat(createdUser.getId(), equalTo(updatedUser.getId()));
        assertThat(createdUser.getName(), equalTo(updatedUser.getName()));
        assertThat(createdUser.getEmail(), equalTo(null));

        createdUser.setEmail("updatedEmail@example.com");
        updatedUser = userService.updateUser(createdUser.getId(), createdUser);

        assertThat(createdUser.getEmail(), equalTo(updatedUser.getEmail()));
    }
}
