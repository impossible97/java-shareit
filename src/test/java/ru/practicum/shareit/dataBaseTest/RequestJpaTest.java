package ru.practicum.shareit.dataBaseTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RequestJpaTest {

    private final ItemRequestRepository requestRepository;
    private final UserRepository userRepository;
    private ItemRequest request;
    private User booker;

    @BeforeEach
    void setRequestRepository() {
        booker = new User();

        booker.setName("Name");
        booker.setEmail("email@email.ru");

        request = new ItemRequest();
        request.setId(1L);
        request.setCreated(LocalDateTime.now());
        request.setDescription("Description");
        request.setUser(booker);

        userRepository.save(booker);
    }

    @Test
    void findAllByUserIdTest() {
        ItemRequest savedRequest = requestRepository.save(request);

        List<ItemRequest> allRequests = requestRepository.findAllByUser_Id(booker.getId(), Sort.by("created").ascending());

        assertThat(allRequests.size(), equalTo(1));
        assertThat(allRequests, hasItem(savedRequest));
    }
}
