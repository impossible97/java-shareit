package ru.practicum.shareit.dataBaseTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserProjection;
import ru.practicum.shareit.user.repository.UserRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserJpaTest {

    private final UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUserRepository() {
        user = new User();

        user.setName("Name");
        user.setEmail("email@email.ru");
    }

    @Test
    void findUserByIdTest() {
        User savedUser = userRepository.save(user);

        UserProjection userById = userRepository.findUserById(savedUser.getId());

        assertThat(savedUser.getId(), equalTo(userById.getId()));
    }

    @Test
    void findByEmailTest() {
        User savedUser = userRepository.save(user);

        User byEmail = userRepository.findByEmail(savedUser.getEmail());

        assertThat(byEmail, notNullValue());
        assertThat(byEmail, equalTo(savedUser));
    }
}
