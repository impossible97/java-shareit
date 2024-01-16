package ru.practicum.server.serviceUnitTest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.server.booking.dto.BookingDto;
import ru.practicum.server.booking.model.Booking;
import ru.practicum.server.booking.model.BookingStatus;
import ru.practicum.server.booking.repository.BookingRepository;
import ru.practicum.server.booking.service.BookingServiceImpl;
import ru.practicum.server.exception.NotFoundException;
import ru.practicum.server.exception.ValidationException;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.item.repository.ItemRepository;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class BookingServiceUnitTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private BookingRepository bookingRepository;
    @InjectMocks
    private BookingServiceImpl bookingService;
    private static User booker;
    private static User owner;
    private static Item item;
    private static Booking booking;
    private static BookingDto bookingDto;

    @BeforeAll
    static void setObjects() {
        booker = new User();
        booker.setId(1L);

        owner = new User();
        owner.setId(2L);

        long itemId = 1L;
        item = new Item();
        item.setId(itemId);
        item.setOwner(owner);

        bookingDto = new BookingDto();
        bookingDto.setItemId(itemId);
        bookingDto.setStart(LocalDateTime.now());
        bookingDto.setEnd(LocalDateTime.now().plusHours(1));

        booking = new Booking();

        booking.setId(1L);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setItem(item);
    }

    @Test
    void addBookingWithEqualUserAndOwnerIdTest() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(owner));
        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));

        assertThrows(NotFoundException.class, () -> bookingService.addBooking(owner.getId(), bookingDto));
    }

    @Test
    void addBookingWithUnavailableStatusTest() {
        item.setAvailable(false);

        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(booker));
        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));

        assertThrows(ValidationException.class, () -> bookingService.addBooking(booker.getId(), bookingDto));
    }

    @Test
    void addBookingWithWrongBookingTimeTest() {
        bookingDto.setEnd(bookingDto.getStart());
        item.setAvailable(true);
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(booker));
        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));

        assertThrows(ValidationException.class, () -> bookingService.addBooking(booker.getId(), bookingDto));
    }

    @Test
    void changeBookingStatusWithNotBookerIdTest() {
        Mockito.when(bookingRepository.findById(anyLong()))
                        .thenReturn(Optional.of(booking));

        assertThrows(NotFoundException.class, () -> bookingService.changeBookingStatus(
                booker.getId(), true, booking.getId()));
    }

    @Test
    void changeBookingStatusWhenItHasAlreadyBeenChangedTest() {
        Mockito.when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));
        booking.setStatus(BookingStatus.APPROVED);

        assertThrows(ValidationException.class, () -> bookingService.changeBookingStatus(
                owner.getId(), true, booking.getId()));
    }
}
