package ru.practicum.shareit.mapperTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingProjection;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.mapper.ItemMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

class ItemMapperTest {

    private static final ItemMapper itemMapper = new ItemMapper();
    private Item item;
    private ItemDto itemDto;
    private BookingProjection lastBooking;
    private BookingProjection nextBooking;
    private User user;
    private Booking booking;
    private ItemRequest itemRequest;

    @BeforeEach
    void setObjects() {
        user = new User();
        user.setId(1L);
        user.setName("Name");
        user.setEmail("email@email.com");

        itemRequest = new ItemRequest();
        itemRequest.setId(1L);

        item = new Item();
        item.setId(1L);
        item.setName("Item");
        item.setDescription("Description");
        item.setAvailable(true);
        item.setOwner(user);
        item.setRequest(itemRequest);

        booking = new Booking();
        booking.setId(1L);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusHours(2));

        lastBooking = new BookingProjection() {
            @Override
            public Long getId() {
                return booking.getId();
            }

            @Override
            public Long getBookerId() {
                return booking.getBooker().getId();
            }

            @Override
            public BookingStatus getStatus() {
                return booking.getStatus();
            }

            @Override
            public LocalDateTime getStart() {
                return booking.getStart();
            }
        };
        nextBooking = new BookingProjection() {
            @Override
            public Long getId() {
                return 2L;
            }

            @Override
            public Long getBookerId() {
                return 2L;
            }

            @Override
            public BookingStatus getStatus() {
                return BookingStatus.APPROVED;
            }

            @Override
            public LocalDateTime getStart() {
                return LocalDateTime.now().plusHours(10);
            }
        };

        itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setLastBooking(lastBooking);
        itemDto.setNextBooking(nextBooking);
        itemDto.setComments(new ArrayList<>());
        itemDto.setRequestId(itemRequest.getId());

    }

    @Test
    void toDtoTest() {
        ItemDto dto = itemMapper.toDto(item, lastBooking, nextBooking, new ArrayList<>());

        assertThat(dto, notNullValue());
        assertThat(dto, equalTo(itemDto));
    }

    @Test
    void toEntityTest() {
        Item entity = itemMapper.toEntity(itemDto, user, itemRequest);

        assertThat(entity, notNullValue());
        assertThat(entity.getName(), equalTo(itemDto.getName()));
        assertThat(entity.getDescription(), equalTo(item.getDescription()));
        assertThat(entity.getRequest(), equalTo(item.getRequest()));
        assertThat(entity.getAvailable(), equalTo(item.getAvailable()));
        assertThat(entity.getOwner(), equalTo(item.getOwner()));
    }
}
