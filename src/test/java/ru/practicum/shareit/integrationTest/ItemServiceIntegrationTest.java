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
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.repository.CommentRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceIntegrationTest {

    @MockBean
    private CommentRepository commentRepository;
    private final BookingService bookingService;
    private final ItemService itemService;
    private final UserService userService;
    private static ItemDto itemDto1;
    private static ItemDto itemDto2;
    private static UserDto userDto;

    @BeforeEach
    void setMocks() {
        Mockito
                .when(commentRepository.findAllByAuthor_Id(anyLong()))
                .thenReturn(new ArrayList<>());
    }

    @BeforeAll
    static void setObjects() {
        itemDto1 = new ItemDto();
        itemDto1.setName("Item");
        itemDto1.setDescription("NewItem");
        itemDto1.setAvailable(true);

        itemDto2 = new ItemDto();
        itemDto2.setName("Item");
        itemDto2.setDescription("NewItem");
        itemDto2.setAvailable(true);

        userDto = new UserDto();
        userDto.setName("Name");
        userDto.setEmail("email@email.com");
    }

    @Test
    void addItemTest() {
        UserDto user = userService.createUser(userDto);
        ItemDto createdItem = itemService.addItem(itemDto1, user.getId());

        assertThat(createdItem, notNullValue());
        assertThat(createdItem.getName(), equalTo(itemDto1.getName()));
        assertThat(createdItem.getDescription(), equalTo(itemDto1.getDescription()));
        assertThat(createdItem.getAvailable(), equalTo(itemDto1.getAvailable()));
    }

    @Test
    void addItemWithWrongUserIdTest() {
        assertThrows(NotFoundException.class, () -> itemService.addItem(itemDto1, 99));
    }

    @Test
    void updateItemTest() {
        // TODO Done
    }

    @Test
    void updateItemWithWrongUserIdTest() {
        UserDto user = userService.createUser(userDto);
        ItemDto createdItem = itemService.addItem(itemDto1, user.getId());
        createdItem.setName("NewName");

        assertThrows(NotFoundException.class, () -> itemService.updateItem(99, createdItem.getId(), createdItem));
    }

    @Test
    void updateItemWithWrongItemIdTest() {
        UserDto user = userService.createUser(userDto);
        ItemDto createdItem = itemService.addItem(itemDto1, user.getId());
        createdItem.setName("NewName");

        assertThrows(NotFoundException.class, () -> itemService.updateItem(user.getId(), 99, createdItem));
    }

    @Test
    void getItemTest() {
        // TODO Done
    }

    @Test
    void getItemWithWrongIdTest() {
        UserDto user = userService.createUser(userDto);

        itemService.addItem(itemDto1, user.getId());

        assertThrows(NotFoundException.class, () -> itemService.getItem(99, user.getId(), 0, 10));
    }

    @Test
    void getAllItemsWithoutBookingsTest() {
        UserDto user = userService.createUser(userDto);

        ItemDto item1 = itemService.addItem(itemDto1, user.getId());
        ItemDto item2 = itemService.addItem(itemDto2, user.getId());

        List<ItemDto> items = itemService.getAll(user.getId(), 0, 10);
        assertThat(items.isEmpty(), equalTo(false));
        assertThat(items, hasItems(item1, item2));

        Mockito.verify(commentRepository, Mockito.times(1)).findAllByAuthor_Id(anyLong());
    }

    @Test
    void getAllItemsWithBookingsTest() {
        UserDto user = new UserDto();
        user.setName("BookerName");
        user.setEmail("booker@email.com");

        UserDto user1 = userService.createUser(userDto);
        UserDto user2 = userService.createUser(user);

        ItemDto item1 = itemService.addItem(itemDto1, user1.getId());
        ItemDto item2 = itemService.addItem(itemDto2, user1.getId());

        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(item1.getId());
        bookingDto.setStart(LocalDateTime.of(2024, 1, 1, 0, 0));
        bookingDto.setEnd(LocalDateTime.of(2025, 1, 1, 0, 0));

        BookingDto booking = bookingService.addBooking(user2.getId(), bookingDto);
        bookingService.changeBookingStatus(user1.getId(), true, booking.getId());

        List<ItemDto> items = itemService.getAll(user1.getId(), 0, 10);

        assertThat(items.isEmpty(), equalTo(false));
        assertThat(items.get(0), not(item1));
        assertThat(items, hasItem(item2));
        assertThat(items.get(0).getNextBooking(), notNullValue());
        Mockito.verify(commentRepository, Mockito.times(1)).findAllByAuthor_Id(anyLong());
    }

    @Test
    void searchItemTest() {
        UserDto user = userService.createUser(userDto);
        ItemDto item = itemService.addItem(itemDto1, user.getId());

        List<ItemDto> items = itemService.searchItem("iTeM", 0, 10);
        assertThat(items.isEmpty(), equalTo(false));
        assertThat(items, hasItem(item));
    }
}
