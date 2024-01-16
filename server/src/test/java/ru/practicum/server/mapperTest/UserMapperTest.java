package ru.practicum.server.mapperTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.server.mapper.UserMapper;
import ru.practicum.server.user.dto.UserDto;
import ru.practicum.server.user.model.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

class UserMapperTest {

    private static final UserMapper userMapper = new UserMapper();
    private User user;
    private UserDto userDto;

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
