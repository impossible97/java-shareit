package ru.practicum.shareit.integrationTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceIntegrationTest {

    private final UserRepository userRepository;
    private final UserService userService;


    @Test
    void createUserTest() {
        UserDto userDto = new UserDto();

        userDto.setName("TestName");
        userDto.setEmail("example@test.com");

        userService.createUser(userDto);
        User user = userRepository.findByEmail(userDto.getEmail());

        assertThat(user, notNullValue());
        assertThat(user.getName(), equalTo(userDto.getName()));
        assertThat(user.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    void getUserTest() {
        UserDto userDto = new UserDto();

        userDto.setName("TestName");
        userDto.setEmail("example@test.com");

        UserDto user = userService.createUser(userDto);
        UserDto resultUser = userService.getUser(user.getId());

        assertThat(resultUser, notNullValue());
        assertThat(resultUser.getId(), equalTo(user.getId()));
        assertThat(resultUser.getName(), equalTo(user.getName()));
        assertThat(resultUser.getEmail(), equalTo(user.getEmail()));
    }

    @Test
    void getAllUsersTest() {
        UserDto userDto1 = new UserDto();
        userDto1.setName("TestName1");
        userDto1.setEmail("example1@test.com");

        UserDto userDto2 = new UserDto();
        userDto2.setName("TestName2");
        userDto2.setEmail("example2@test.com");

        UserDto user1 = userService.createUser(userDto1);
        UserDto user2 = userService.createUser(userDto2);

        List<UserDto> allUsers = userService.getAllUsers();

        assertThat(allUsers, notNullValue());
        assertThat(allUsers.size(), equalTo(2));
        assertThat(allUsers.get(0).getName(), equalTo(userDto1.getName()));
        assertThat(allUsers, hasItem(user1));
        assertThat(allUsers, hasItem(user2));
    }

    @Test
    void deleteUserTest() {
        UserDto userDto = new UserDto();
        userDto.setName("TestName1");
        userDto.setEmail("example1@test.com");

        UserDto user = userService.createUser(userDto);
        userService.deleteUser(user.getId());

        assertThat(userService.getAllUsers(), empty());
    }

    @Test
    void updateUserTest() {
        UserDto userDto = new UserDto();
        userDto.setName("TestName1");
        userDto.setEmail("example1@test.com");

        UserDto user = userService.createUser(userDto);
        user.setName("updatedName");
        user.setEmail("updatedEmail@example.com");

        UserDto updatedUser = userService.updateUser(user.getId(), user);

        assertThat(user.getId(), equalTo(updatedUser.getId()));
        assertThat(user.getName(), equalTo(updatedUser.getName()));
        assertThat(user.getEmail(), equalTo(updatedUser.getEmail()));
    }
}
