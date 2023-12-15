package ru.practicum.shareit.integrationTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceIntegrationTest {

    private final ItemService itemService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ItemRequestRepository requestRepository;

    @BeforeEach
    void setMocks() {
        User user = new User();
        ItemRequest request = new ItemRequest();
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        Mockito.when(requestRepository.findById(anyLong()))
                .thenReturn(Optional.of(request));
    }

    @Test
    void addItemTest() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Item");
        itemDto.setDescription("NewItem");
        itemDto.setAvailable(true);
        itemDto.setRequestId(1L);

        ItemDto createdItem = itemService.addItem(itemDto, anyLong());

        assertThat(createdItem, notNullValue());
        assertThat(createdItem.getName(), equalTo(itemDto.getName()));
        assertThat(createdItem.getDescription(), equalTo(itemDto.getDescription()));
        assertThat(createdItem.getAvailable(), equalTo(itemDto.getAvailable()));
        Mockito.verify(userRepository, Mockito.times(1)).findById(anyLong());
    }

    @Test
    void updateItemTest() {

    }
}
