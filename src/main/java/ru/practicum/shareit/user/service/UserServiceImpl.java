package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.mapper.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public UserDto getUser(long userId) {
        return userMapper.toDto(userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id= " + userId + " не найден")));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        return userMapper.toDto(userRepository.save(userMapper.toEntity(userDto)));
    }

    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.orElseThrow(() -> new NotFoundException("Пользователь с id= " + userId + " не найден"));
        user.setId(userId);
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
    }
}