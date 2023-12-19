package ru.practicum.shareit.integrationTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class BookingServiceIntegrationTest {
    private final BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService;
    private static UserDto userDto1;
    private static UserDto userDto2;
    private static BookingDto bookingDto1;
    private static BookingDto bookingDto2;
    private static ItemDto itemDto1;
    private static ItemDto itemDto2;
    private static UserDto booker;
    private static UserDto owner;
    private static ItemDto item1;
    private static ItemDto item2;

    @BeforeAll
    static void setObjects() {
        itemDto1 = new ItemDto();
        itemDto1.setName("Item1");
        itemDto1.setDescription("NewItem1");
        itemDto1.setAvailable(true);

        itemDto2 = new ItemDto();
        itemDto2.setName("Item2");
        itemDto2.setDescription("NewItem2");
        itemDto2.setAvailable(true);

        userDto1 = new UserDto();
        userDto1.setName("Name1");
        userDto1.setEmail("email1@email.com");

        userDto2 = new UserDto();
        userDto2.setName("Name2");
        userDto2.setEmail("email2@email.com");

        bookingDto1 = new BookingDto();
        bookingDto1.setStart(LocalDateTime.of(2024, 1, 1, 0, 0));
        bookingDto1.setEnd(LocalDateTime.of(2025, 1, 1, 0, 0));

        bookingDto2 = new BookingDto();
        bookingDto2.setStart(LocalDateTime.of(2026, 1, 1, 0, 0));
        bookingDto2.setEnd(LocalDateTime.of(2027, 1, 1, 0, 0));
    }

    @BeforeEach
    void setBookingService() {
        booker = userService.createUser(userDto1);
        owner = userService.createUser(userDto2);

        item1 = itemService.addItem(itemDto1, owner.getId());
        item2 = itemService.addItem(itemDto2, owner.getId());

        bookingDto1.setStart(LocalDateTime.of(2024, 1, 1, 0, 0));
        bookingDto1.setEnd(LocalDateTime.of(2025, 1, 1, 0, 0));

        bookingDto2.setStart(LocalDateTime.of(2026, 1, 1, 0, 0));
        bookingDto2.setEnd(LocalDateTime.of(2027, 1, 1, 0, 0));
    }

    @Test
    void addBookingWithWrongUserIdTest () {
        bookingDto1.setItemId(item1.getId());

        assertThrows(NotFoundException.class, () -> bookingService.addBooking(99, bookingDto1));
    }

    @Test
    void addBookingWithWrongItemIdTest() {
        bookingDto1.setItemId(99L);
        assertThrows(NotFoundException.class, () -> bookingService.addBooking(booker.getId(), bookingDto1));
    }

    @Test
    void addBookingWithOwnerIdTest() {
        bookingDto1.setItemId(item1.getId());

        assertThrows(NotFoundException.class, () -> bookingService.addBooking(owner.getId(), bookingDto1));
    }

    @Test
    void addBookingWithUnavailableStatusTest() { // Можно в Юнит
        item1.setAvailable(false);
        itemService.updateItem(owner.getId(), item1.getId(), item1);
        bookingDto1.setItemId(item1.getId());

        assertThrows(ValidationException.class, () -> bookingService.addBooking(booker.getId(), bookingDto1));
    }

    @Test
    void addBookingWithWrongBookingTimeTest() { // Можно в Юнит
        bookingDto1.setItemId(item1.getId());
        bookingDto1.setEnd(bookingDto1.getStart());

        assertThrows(ValidationException.class, () -> bookingService.addBooking(booker.getId(), bookingDto1));
    }

    @Test
    void addBookingTest() {
        bookingDto1.setItemId(item1.getId());

        BookingDto createdBooking = bookingService.addBooking(booker.getId(), bookingDto1);

        assertThat(createdBooking, notNullValue());
        assertThat(bookingDto1.getStart(), equalTo(createdBooking.getStart()));
        assertThat(bookingDto1.getEnd(), equalTo(createdBooking.getEnd()));
        assertThat(bookingDto1.getItemId(), equalTo(createdBooking.getItemId()));
    }

    @Test
    void changeBookingStatusWithWrongBookingIdTest() {
        bookingDto1.setItemId(item1.getId());

        bookingService.addBooking(booker.getId(), bookingDto1);

        assertThrows(NotFoundException.class, () -> bookingService.changeBookingStatus(
                owner.getId(),
                true,
                99L));
    }

    @Test
    void changeBookingStatusWithNotBookerIdTest() {
        bookingDto1.setItemId(item1.getId());

        BookingDto createdBooking = bookingService.addBooking(booker.getId(), bookingDto1);

        assertThrows(NotFoundException.class, () -> bookingService.changeBookingStatus(
                booker.getId(),
                true,
                createdBooking.getId()));
    }

    @Test
    void changeBookingStatusWhenItHasAlreadyBeenChangedTest() {
        bookingDto1.setItemId(item1.getId());

        BookingDto createdBooking = bookingService.addBooking(booker.getId(), bookingDto1);
        bookingService.changeBookingStatus(owner.getId(), true, createdBooking.getId());

        assertThrows(ValidationException.class, () -> bookingService.changeBookingStatus(
                owner.getId(),
                true,
                createdBooking.getId()));
    }

    @Test
    void changeBookingStatusTest() {
        bookingDto1.setItemId(item1.getId());

        BookingDto createdBooking = bookingService.addBooking(booker.getId(), bookingDto1);
        BookingDto changedBooking1 = bookingService.changeBookingStatus(owner.getId(), false, createdBooking.getId());

        assertThat(changedBooking1, notNullValue());
        assertThat(changedBooking1.getStatus(), equalTo(BookingStatus.REJECTED));

        BookingDto changedBooking2 = bookingService.changeBookingStatus(owner.getId(), true, createdBooking.getId());

        assertThat(changedBooking2, notNullValue());
        assertThat(changedBooking2.getStatus(), equalTo(BookingStatus.APPROVED));
    }

    @Test
    void getBookingWithWrongBookingIdTest() {
        bookingDto1.setItemId(item1.getId());

        bookingService.addBooking(booker.getId(), bookingDto1);

        assertThrows(NotFoundException.class, () -> bookingService.getBooking(99L, booker.getId()));
    }

    @Test
    void getBookingWithWrongBookerIdOrOwnerIdTest() {
        bookingDto1.setItemId(item1.getId());

        BookingDto createdBooking = bookingService.addBooking(booker.getId(), bookingDto1);

        assertThrows(NotFoundException.class, () -> bookingService.getBooking(
                createdBooking.getId(),
                99L));
    }

    @Test
    void getBookingTest() {
        bookingDto1.setItemId(item1.getId());

        BookingDto createdBooking = bookingService.addBooking(booker.getId(), bookingDto1);

        BookingDto booking = bookingService.getBooking(createdBooking.getId(), createdBooking.getBooker().getId());

        assertThat(booking, notNullValue());
        assertThat(createdBooking.getId(), equalTo(booking.getId()));
        assertThat(createdBooking.getItemId(), equalTo(booking.getItemId()));
        assertThat(createdBooking.getStatus(), equalTo(booking.getStatus()));
    }

    @Test
    void getBookingsByBookerWithWrongUserIdTest() {
        assertThrows(NotFoundException.class, () -> bookingService.getAllBookingsByBooker(
                99L, BookingStatus.APPROVED, 0, 10));
    }

    @Test
    void getAllBookingsByBookerTest() {
        bookingDto1.setItemId(item1.getId());
        bookingDto2.setItemId(item2.getId());

        bookingService.addBooking(booker.getId(), bookingDto1);
        bookingService.addBooking(booker.getId(), bookingDto2);

        List<BookingDto> allBookingsByBooker = bookingService.getAllBookingsByBooker(
                booker.getId(), BookingStatus.ALL, 0, 10);

        assertThat(allBookingsByBooker, notNullValue());
        assertThat(allBookingsByBooker.size(), equalTo(2));
    }

    @Test
    void getFutureBookingsByBookerTest() {
        bookingDto1.setItemId(item1.getId());
        bookingDto2.setItemId(item2.getId());

        bookingService.addBooking(booker.getId(), bookingDto1);
        bookingService.addBooking(booker.getId(), bookingDto2);

        List<BookingDto> allBookingsByBooker = bookingService.getAllBookingsByBooker(
                booker.getId(), BookingStatus.FUTURE, 0, 10);

        assertThat(allBookingsByBooker, notNullValue());
        assertThat(allBookingsByBooker.size(), equalTo(2));
    }

    @Test
    void getCurrentBookingsByBookerTest() {
        bookingDto1.setItemId(item1.getId());
        bookingDto1.setStart(LocalDateTime.now());

        bookingDto2.setItemId(item2.getId());

        BookingDto booking = bookingService.addBooking(booker.getId(), bookingDto1);
        bookingService.addBooking(booker.getId(), bookingDto2);

        List<BookingDto> allBookingsByBooker = bookingService.getAllBookingsByBooker(
                booker.getId(), BookingStatus.CURRENT, 0, 10);

        assertThat(allBookingsByBooker, notNullValue());
        assertThat(allBookingsByBooker.size(), equalTo(1));
    }

    @Test
    void getPastBookingsByBookerTest() {
        bookingDto1.setItemId(item1.getId());
        LocalDateTime now = LocalDateTime.now();
        bookingDto1.setStart(now);
        bookingDto1.setEnd(now.plusNanos(5));

        bookingDto2.setItemId(item2.getId());

        bookingService.addBooking(booker.getId(), bookingDto1);
        bookingService.addBooking(booker.getId(), bookingDto2);

        List<BookingDto> allBookingsByBooker = bookingService.getAllBookingsByBooker(
                booker.getId(), BookingStatus.PAST, 0, 10);

        assertThat(allBookingsByBooker, notNullValue());
        assertThat(allBookingsByBooker.size(), equalTo(1));
    }

    @Test
    void getDefaultBookingsByBookerTest() {
        bookingDto1.setItemId(item1.getId());
        bookingDto2.setItemId(item2.getId());

        bookingService.addBooking(booker.getId(), bookingDto1);
        bookingService.addBooking(booker.getId(), bookingDto2);

        List<BookingDto> allBookingsByBooker = bookingService.getAllBookingsByBooker(
                booker.getId(), BookingStatus.APPROVED, 0, 10);

        assertThat(allBookingsByBooker, notNullValue());
        assertThat(allBookingsByBooker.isEmpty(), equalTo(true));
    }

    @Test
    void getBookingsByOwnerWithWrongOwnerId() {
        assertThrows(NotFoundException.class, () -> bookingService.getAllBookingsByOwner(
                99L, BookingStatus.ALL, 0, 10));
    }

    @Test
    void getAllBookingsByOwnerIdTest() {
        bookingDto1.setItemId(item1.getId());
        bookingDto2.setItemId(item2.getId());

        bookingService.addBooking(booker.getId(), bookingDto1);
        bookingService.addBooking(booker.getId(), bookingDto2);

        List<BookingDto> allBookingsByOwner = bookingService.getAllBookingsByOwner(
                owner.getId(), BookingStatus.ALL, 0, 10);

        assertThat(allBookingsByOwner, notNullValue());
        assertThat(allBookingsByOwner.size(), equalTo(2));
    }

    @Test
    void getFutureBookingsByOwnerTest() {
        bookingDto1.setItemId(item1.getId());
        bookingDto2.setItemId(item2.getId());

        bookingService.addBooking(booker.getId(), bookingDto1);
        bookingService.addBooking(booker.getId(), bookingDto2);

        List<BookingDto> allBookingsByBooker = bookingService.getAllBookingsByOwner(
                owner.getId(), BookingStatus.FUTURE, 0, 10);

        assertThat(allBookingsByBooker, notNullValue());
        assertThat(allBookingsByBooker.size(), equalTo(2));
    }

    @Test
    void getCurrentBookingsByOwnerTest() {
        bookingDto1.setItemId(item1.getId());
        bookingDto1.setStart(LocalDateTime.now());

        bookingDto2.setItemId(item2.getId());

        BookingDto booking = bookingService.addBooking(booker.getId(), bookingDto1);
        bookingService.addBooking(booker.getId(), bookingDto2);

        List<BookingDto> allBookingsByBooker = bookingService.getAllBookingsByOwner(
                owner.getId(), BookingStatus.CURRENT, 0, 10);

        assertThat(allBookingsByBooker, notNullValue());
        assertThat(allBookingsByBooker.size(), equalTo(1));
    }

    @Test
    void getPastBookingsByOwnerTest() {
        bookingDto1.setItemId(item1.getId());
        LocalDateTime now = LocalDateTime.now();
        bookingDto1.setStart(now);
        bookingDto1.setEnd(now.plusNanos(5));

        bookingDto2.setItemId(item2.getId());

        bookingService.addBooking(booker.getId(), bookingDto1);
        bookingService.addBooking(booker.getId(), bookingDto2);

        List<BookingDto> allBookingsByBooker = bookingService.getAllBookingsByOwner(
                owner.getId(), BookingStatus.PAST, 0, 10);

        assertThat(allBookingsByBooker, notNullValue());
        assertThat(allBookingsByBooker.size(), equalTo(1));
    }

    @Test
    void getDefaultBookingsByOwnerTest() {
        bookingDto1.setItemId(item1.getId());
        bookingDto2.setItemId(item2.getId());

        bookingService.addBooking(booker.getId(), bookingDto1);
        bookingService.addBooking(booker.getId(), bookingDto2);

        List<BookingDto> allBookingsByBooker = bookingService.getAllBookingsByOwner(
                owner.getId(), BookingStatus.APPROVED, 0, 10);

        assertThat(allBookingsByBooker, notNullValue());
        assertThat(allBookingsByBooker.isEmpty(), equalTo(true));
    }
}
