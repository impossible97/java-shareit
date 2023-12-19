package ru.practicum.shareit.integrationTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class CommentIntegrationTest {

    private final BookingService bookingService;
    private final ItemService itemService;
    private final UserService userService;
    private static ItemDto itemDto;
    private static UserDto userDto;

    @BeforeAll
    static void setObjects() {
        itemDto = new ItemDto();
        itemDto.setName("Item");
        itemDto.setDescription("NewItem");
        itemDto.setAvailable(true);

        userDto = new UserDto();
        userDto.setName("Name");
        userDto.setEmail("email@email.com");
    }

    @Test
    void addCommentIfBookingsIsEmptyTest() {
        UserDto user = userService.createUser(userDto);
        ItemDto item = itemService.addItem(itemDto, user.getId());

        CommentDto comment = new CommentDto();
        comment.setText("someText");

        assertThrows(ValidationException.class, () -> itemService.addComment(item.getId(), user.getId(), comment));
    }

    @Test
    void addCommentWithWrongUserIdTest() {
        UserDto user = new UserDto();
        user.setName("BookerName");
        user.setEmail("booker@email.com");

        UserDto user1 = userService.createUser(userDto);
        UserDto user2 = userService.createUser(user);

        ItemDto item = itemService.addItem(itemDto, user1.getId());

        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(item.getId());
        bookingDto.setStart(LocalDateTime.of(2024, 1, 1, 0, 0));
        bookingDto.setEnd(LocalDateTime.of(2025, 1, 1, 0, 0));
        BookingDto booking = bookingService.addBooking(user2.getId(), bookingDto);
        bookingService.changeBookingStatus(user1.getId(), true, booking.getId());

        CommentDto comment = new CommentDto();
        comment.setText("someText");

        assertThrows(ValidationException.class, () -> itemService.addComment(item.getId(), 99, comment));
    }

    @Test
    void addCommentForFutureBookingTest() {
        UserDto user = new UserDto();
        user.setName("BookerName");
        user.setEmail("booker@email.com");

        UserDto user1 = userService.createUser(userDto);
        UserDto user2 = userService.createUser(user);

        ItemDto item = itemService.addItem(itemDto, user1.getId());

        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(item.getId());
        bookingDto.setStart(LocalDateTime.of(2025, 1, 1, 0, 0));
        bookingDto.setEnd(LocalDateTime.of(2026, 1, 1, 0, 0));
        BookingDto booking = bookingService.addBooking(user2.getId(), bookingDto);
        bookingService.changeBookingStatus(user1.getId(), true, booking.getId());

        CommentDto comment = new CommentDto();
        comment.setText("someText");

        assertThrows(ValidationException.class, () -> itemService.addComment(item.getId(), user.getId(), comment));
    }

    @Test
    void addCommentTest() {
        UserDto user = new UserDto();
        user.setName("BookerName");
        user.setEmail("booker@email.com");

        UserDto user1 = userService.createUser(userDto);
        UserDto user2 = userService.createUser(user);

        ItemDto item = itemService.addItem(itemDto, user1.getId());

        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(item.getId());
        LocalDateTime now = LocalDateTime.now();
        bookingDto.setStart(now);
        bookingDto.setEnd(now.plusNanos(10));
        BookingDto booking = bookingService.addBooking(user2.getId(), bookingDto);
        bookingService.changeBookingStatus(user1.getId(), true, booking.getId());

        CommentDto comment = new CommentDto();
        comment.setText("someText");

        itemService.addComment(item.getId(), user2.getId(), comment);

        assertThat(item.getComments(), notNullValue());
        assertThat(itemService.getItem(item.getId(), user1.getId(), 0, 10).getComments().get(0).getText(), equalTo(comment.getText()));
    }
}
