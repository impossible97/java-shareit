package ru.practicum.shareit.integrationTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceIntegrationTest {
    private final BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService;
    private static UserDto userDto1;
    private static UserDto userDto2;
    private static BookingDto bookingDto;
    private static ItemDto itemDto1;
    private static ItemDto itemDto2;

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

        userDto1 = new UserDto();
        userDto1.setName("Name");
        userDto1.setEmail("email@email.com");

        userDto2 = new UserDto();
        userDto2.setName("Name");
        userDto2.setEmail("email@email.com");

        bookingDto = new BookingDto();
        bookingDto.setStart(LocalDateTime.of(2024, 1, 1, 0, 0));
        bookingDto.setEnd(LocalDateTime.of(2025, 1, 1, 0, 0));

    }

    @Test
    void addBookingWithWrongUserIdTest () {
        UserDto createdUser1 = userService.createUser(userDto1);
        UserDto createdUser2 = userService.createUser(userDto1);
        ItemDto item = itemService.addItem(itemDto1, createdUser1.getId());

        bookingDto.setItemId(item.getId());

        bookingService.addBooking(99, bookingDto);

    }
}
