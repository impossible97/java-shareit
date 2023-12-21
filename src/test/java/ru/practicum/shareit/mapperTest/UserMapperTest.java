package ru.practicum.shareit.mapperTest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.mapper.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserMapperTest {

    private static UserMapper userMapper;
    private User user;
    private UserDto userDto;

    @BeforeAll
    static void setMapper() {
        userMapper = new UserMapper();
    }

    @BeforeEach
    void setObjects() {
        user = new User();
        user.setId(1L);
        user.setName("Name");
        user.setEmail("email@email.com");

        userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
    }

    @Test
    void toDtoTest() {
        UserDto dto = userMapper.toDto(user);

        assertThat(dto, notNullValue());
        assertThat(dto, equalTo(userDto));
    }

    @Test
    void toEntityTest() {
        User entity = userMapper.toEntity(userDto);

        assertThat(entity, notNullValue());
        assertThat(entity.getName(), equalTo(user.getName()));
        assertThat(entity.getEmail(), equalTo(user.getEmail()));
    }
}
