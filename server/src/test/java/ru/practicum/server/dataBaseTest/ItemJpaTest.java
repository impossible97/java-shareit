package ru.practicum.server.dataBaseTest;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.item.model.ItemProjection;
import ru.practicum.server.item.model.ItemRequestProjection;
import ru.practicum.server.item.repository.ItemRepository;
import ru.practicum.server.request.model.ItemRequest;
import ru.practicum.server.request.repository.ItemRequestRepository;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemJpaTest {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemRequestRepository requestRepository;
    private Item item;
    private ItemRequest request;

    @BeforeEach
    void setItemRepository() {
        User owner = new User();

        owner.setName("Owner");
        owner.setEmail("owner@mail.ru");

        User booker = new User();

        booker.setName("Name");
        booker.setEmail("email@email.ru");

        request = new ItemRequest();
        request.setId(1L);
        request.setCreated(LocalDateTime.now());
        request.setDescription("Description");
        request.setUser(booker);

        item = new Item();

        item.setName("Item");
        item.setDescription("Description");
        item.setAvailable(true);
        item.setOwner(owner);
        item.setRequest(request);

        userRepository.save(owner);
        userRepository.save(booker);
        requestRepository.save(request);
    }

    @Test
    void findByTextTest() {
        Item savedItem = itemRepository.save(item);

        List<Item> items = itemRepository.findByText("ItEm", PageRequest.of(0, 10));

        assertThat(items.size(), equalTo(1));
        assertThat(items, hasItem(savedItem));
    }

    @Test
    void findItemByIdTest() {
        Item savedItem = itemRepository.save(item);

        ItemProjection itemById = itemRepository.findItemById(savedItem.getId());

        assertThat(itemById, notNullValue());
        assertThat(itemById.getId(), equalTo(savedItem.getId()));
        assertThat(itemById.getName(), equalTo(savedItem.getName()));
    }

    @Test
    void findAllByRequestIdTest() {
        Item savedItem = itemRepository.save(item);

        List<ItemRequestProjection> byRequestId = itemRepository.findAllByRequest_Id(request.getId());

        assertThat(byRequestId.size(), equalTo(1));
    }
}
