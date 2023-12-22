package ru.practicum.shareit.dataBaseTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingProjection;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingJpaTest {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private Booking booking;
    private User booker;
    private User owner;
    private Item item;

    @BeforeEach
    void setBookingRepository() {
        booker = new User();

        booker.setName("Name");
        booker.setEmail("email@email.ru");

        owner = new User();

        owner.setName("Owner");
        owner.setEmail("owner@mail.ru");

        item = new Item();

        item.setName("Item");
        item.setDescription("Description");
        item.setAvailable(true);
        item.setOwner(owner);

        booking = new Booking();
        booking.setStatus(BookingStatus.APPROVED);
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusMinutes(10));

        userRepository.save(booker);
        userRepository.save(owner);
        itemRepository.save(item);
    }

    @Test
    void saveBookingTest() {
        Booking savedBooking = bookingRepository.save(booking);

        booking.setId(1L);
        assertThat(savedBooking, notNullValue());
        assertThat(savedBooking, equalTo(booking));
    }

    @Test
    void findAllByBookerIdAndStatusTest() {
        Booking savedBooking = bookingRepository.save(booking);

        List<Booking> allByBookerIdAndStatus = bookingRepository.findAllByBookerIdAndStatus(1L, BookingStatus.APPROVED, PageRequest.of(0, 10));

        assertThat(allByBookerIdAndStatus.size(), equalTo(1));
        assertThat(allByBookerIdAndStatus, hasItem(savedBooking));
    }

    @Test
    void findAllByBookerIdTest() {
        Booking savedBooking = bookingRepository.save(booking);

        List<Booking> allByBookerId = bookingRepository.findAllByBookerId(booker.getId(), PageRequest.of(0, 10));

        assertThat(allByBookerId.size(), equalTo(1));
        assertThat(allByBookerId, hasItem(savedBooking));
    }

    @Test
    void findAllByBooker_IdAndEndBeforeTest() {
        booking.setStart(LocalDateTime.of(2022, 12,12, 0, 0));
        booking.setEnd(LocalDateTime.of(2022, 12, 12, 1, 0));
        Booking savedBooking = bookingRepository.save(booking);

        List<Booking> allByBooker_idAndEndBefore = bookingRepository.findAllByBooker_IdAndEndBefore(
                booking.getId(), LocalDateTime.now(), PageRequest.of(0, 10));

        assertThat(allByBooker_idAndEndBefore.size(), equalTo(1));
        assertThat(allByBooker_idAndEndBefore, hasItem(savedBooking));
    }

    @Test
    void findAllByBooker_IdAndStartAfterTest() {
        booking.setStart(LocalDateTime.of(2024, 12,12, 0, 0));
        booking.setEnd(LocalDateTime.of(2024, 12, 12, 1, 0));
        Booking savedBooking = bookingRepository.save(booking);

        List<Booking> allByBooker_idAndStartAfter = bookingRepository.findAllByBooker_IdAndStartAfter(
                booking.getId(), LocalDateTime.now(), PageRequest.of(0, 10));

        assertThat(allByBooker_idAndStartAfter.size(), equalTo(1));
        assertThat(allByBooker_idAndStartAfter, hasItem(savedBooking));
    }

    @Test
    void findByItem_Owner_IdAndStatusTest() {
        Booking savedBooking = bookingRepository.save(booking);

        List<Booking> byItem_owner_idAndStatus = bookingRepository.findByItem_Owner_IdAndStatus(
                owner.getId(), BookingStatus.APPROVED, PageRequest.of(0, 10));

        assertThat(byItem_owner_idAndStatus.size(), equalTo(1));
        assertThat(byItem_owner_idAndStatus, hasItem(savedBooking));
    }

    @Test
    void findAllByItem_Owner_IdTest() {
        Booking savedBooking = bookingRepository.save(booking);

        List<Booking> allByItem_owner_id = bookingRepository.findAllByItem_Owner_Id(
                owner.getId(), PageRequest.of(0, 10));

        assertThat(allByItem_owner_id.size(), equalTo(1));
        assertThat(allByItem_owner_id, hasItem(savedBooking));
    }

    @Test
    void findAllByItem_Owner_IdAndEndBeforeTest() {
        Booking savedBooking = bookingRepository.save(booking);

        List<Booking> allByItem_owner_idAndEndBefore = bookingRepository.findAllByItem_Owner_IdAndEndBefore(
                owner.getId(), LocalDateTime.now(), PageRequest.of(0, 10));

        assertThat(allByItem_owner_idAndEndBefore.size(), equalTo(0));
        assertThat(allByItem_owner_idAndEndBefore, not(savedBooking));
    }

    @Test
    void findAllByItem_Owner_IdAndStartAfterTest() {
        Booking savedBooking = bookingRepository.save(booking);

        List<Booking> allByItem_owner_idAndStartAfter = bookingRepository.findAllByItem_Owner_IdAndStartAfter(
                owner.getId(), LocalDateTime.now().minusHours(3), PageRequest.of(0, 10));

        assertThat(allByItem_owner_idAndStartAfter.size(), equalTo(1));
        assertThat(allByItem_owner_idAndStartAfter, hasItem(savedBooking));
    }

    @Test
    void findAllByBooker_IdAndStartBeforeAndEndAfterTest() {
        Booking savedBooking = bookingRepository.save(booking);
        booking.setStart(LocalDateTime.now().minusHours(1));
        booking.setEnd(LocalDateTime.now().plusHours(1));

        List<Booking> allByBooker_idAndStartBeforeAndEndAfter = bookingRepository.findAllByBooker_IdAndStartBeforeAndEndAfter(
                booker.getId(), LocalDateTime.now(), LocalDateTime.now().plusSeconds(100), PageRequest.of(0, 10));

        assertThat(allByBooker_idAndStartBeforeAndEndAfter.size(), equalTo(1));
        assertThat(allByBooker_idAndStartBeforeAndEndAfter, hasItem(savedBooking));
    }

    @Test
    void findAllByItem_Owner_IdAndStartBeforeAndEndAfterTest() {
        Booking savedBooking = bookingRepository.save(booking);
        booking.setStart(LocalDateTime.now().minusHours(1));
        booking.setEnd(LocalDateTime.now().plusHours(1));

        List<Booking> allByItem_owner_idAndStartBeforeAndEndAfter = bookingRepository.findAllByItem_Owner_IdAndStartBeforeAndEndAfter(
                owner.getId(), LocalDateTime.now(), LocalDateTime.now().plusSeconds(100), PageRequest.of(0, 10));

        assertThat(allByItem_owner_idAndStartBeforeAndEndAfter.size(), equalTo(1));
        assertThat(allByItem_owner_idAndStartBeforeAndEndAfter, hasItem(savedBooking));
    }

    @Test
    void findFirstByItem_Owner_IdAndStartBeforeAndStatusTest() {
        Booking savedBooking = bookingRepository.save(booking);

        BookingProjection start = bookingRepository.findFirstByItem_Owner_IdAndStartBeforeAndStatus(
                owner.getId(), LocalDateTime.now().plusSeconds(10), BookingStatus.APPROVED, Sort.by("start").descending());

        assertThat(start.getId(), equalTo(savedBooking.getId()));
        assertThat(start.getStart().format(DateTimeFormatter.ISO_DATE), equalTo(savedBooking.getStart().format(DateTimeFormatter.ISO_DATE)));
    }

    @Test
    void findFirstByItem_Owner_IdAndStartAfterAndStatusTest() {
        Booking savedBooking = bookingRepository.save(booking);

        BookingProjection start = bookingRepository.findFirstByItem_Owner_IdAndStartAfterAndStatus(
                owner.getId(), LocalDateTime.now().minusHours(1), BookingStatus.APPROVED, Sort.by("start").descending());

        assertThat(start.getId(), equalTo(savedBooking.getId()));
        assertThat(start.getStart().format(DateTimeFormatter.ISO_DATE), equalTo(savedBooking.getStart().format(DateTimeFormatter.ISO_DATE)));
    }

    @Test
    void existsByItem_IdTest() {
        Booking savedBooking = bookingRepository.save(booking);

        Boolean isExists = bookingRepository.existsByItem_Id(item.getId());

        assertThat(isExists, notNullValue());
        assertThat(isExists, equalTo(true));
    }
    
    @Test
    void existsByBooker_IdAndItem_IdTest() {
        Booking savedBooking = bookingRepository.save(booking);

        Boolean isExists = bookingRepository.existsByBooker_IdAndItem_Id(booking.getId(), item.getId());

        assertThat(isExists, notNullValue());
        assertThat(isExists, equalTo(true));
    }

    @Test
    void findByItem_IdAndBooker_IdAndStatusTest() {
        Booking savedBooking = bookingRepository.save(booking);

        List<Booking> bookings = bookingRepository.findByItem_IdAndBooker_IdAndStatus(item.getId(), booker.getId(), BookingStatus.APPROVED);

        assertThat(bookings, notNullValue());
        assertThat(bookings, hasItem(savedBooking));
    }

    @Test
    void existsByBooker_IdAndItem_IdAndStartBeforeTest() {
        Booking savedBooking = bookingRepository.save(booking);

        Boolean isExists = bookingRepository.existsByBooker_IdAndItem_IdAndStartBefore(
                booker.getId(), item.getId(), LocalDateTime.now().plusSeconds(10));

        assertThat(isExists, equalTo(true));
    }
}