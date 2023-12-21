package ru.practicum.shareit.mapperTest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemProjection;
import ru.practicum.shareit.mapper.BookingMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserProjection;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class BookingMapperTest {

    private User user;
    private Item item;
    private BookingDto bookingDto;
    private Booking booking;
    private UserProjection userProjection;
    private ItemProjection itemProjection;
    private static BookingMapper bookingMapper;

    @BeforeAll
    static void setMapper() {
        bookingMapper = new BookingMapper();
    }
    @BeforeEach
    void setObjects() {
        user = new User();
        user.setId(1L);
        user.setName("Name");
        user.setEmail("email@email.com");

        item = new Item();
        item.setId(1L);
        item.setName("Item");
        item.setDescription("Description");
        item.setAvailable(true);

        userProjection = () -> user.getId();

        itemProjection = new ItemProjection() {
            @Override
            public Long getId() {
                return item.getId();
            }

            @Override
            public String getName() {
                return item.getName();
            }
        };

        bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setStart(LocalDateTime.now());
        bookingDto.setEnd(LocalDateTime.now().plusHours(3));
        bookingDto.setStatus(BookingStatus.APPROVED);
        bookingDto.setItemId(item.getId());
        bookingDto.setItem(itemProjection);
        bookingDto.setBooker(userProjection);

        booking = new Booking();
        booking.setId(bookingDto.getId());
        booking.setStatus(bookingDto.getStatus());
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
    }

    @Test
    void toDtoTest() {
        BookingDto dto = bookingMapper.toDto(booking, userProjection, itemProjection);

        assertThat(dto, notNullValue());
        assertThat(dto, equalTo(bookingDto));
    }

    @Test
    void toEntityTest() {
        Booking entity = bookingMapper.toEntity(bookingDto, user, item);

        assertThat(entity, notNullValue());
        assertThat(entity.getStatus(), equalTo(booking.getStatus()));
        assertThat(entity.getStart(), equalTo(booking.getStart()));
        assertThat(entity.getEnd(), equalTo(booking.getEnd()));
        assertThat(entity.getBooker(), equalTo(booking.getBooker()));
        assertThat(entity.getItem(), equalTo(booking.getItem()));
    }
}
