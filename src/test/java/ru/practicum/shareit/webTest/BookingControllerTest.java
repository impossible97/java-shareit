package ru.practicum.shareit.webTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class BookingControllerTest {

    @MockBean
    private BookingService bookingService;
    private final MockMvc mvc;
    private final ObjectMapper mapper;
    private BookingDto bookingDto;
    private ItemDto itemDto;
    private UserDto userDto;

    @BeforeEach
    void setObjects() {
        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Name");
        itemDto.setDescription("Description");
        itemDto.setAvailable(true);

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("Name");
        userDto.setEmail("email@email.com");

        bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setItemId(itemDto.getId());
        bookingDto.setStart(LocalDateTime.of(2024, 1, 1, 0, 0));
        bookingDto.setEnd(LocalDateTime.of(2025, 1, 1, 0, 0));
    }

    @Test
    void addBookingTest() throws Exception {
        Mockito
                .when(bookingService.addBooking(anyLong(), any()))
                .thenReturn(bookingDto);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .header("X-Sharer-User-Id", userDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.itemId", is(bookingDto.getItemId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingDto.getStart().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$.end", is(bookingDto.getEnd().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))));
    }

    @Test
    void addBookingWithPastDateTest() throws Exception {
        Mockito
                .when(bookingService.addBooking(anyLong(), any()))
                .thenReturn(bookingDto);
        bookingDto.setStart(LocalDateTime.now().minusYears(2));

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .header("X-Sharer-User-Id", userDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    void addBookingWithPastEndAndEndTest() throws Exception {
        Mockito
                .when(bookingService.addBooking(anyLong(), any()))
                .thenReturn(bookingDto);
        bookingDto.setEnd((LocalDateTime.now().minusYears(2)));

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .header("X-Sharer-User-Id", userDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    void changeBookingStatusTest() throws Exception{
        Mockito
                .when(bookingService.changeBookingStatus(anyLong(), any(), anyLong()))
                .thenReturn(bookingDto);

        mvc.perform(patch("/bookings/{bookingId}", bookingDto.getId())
                        .content(mapper.writeValueAsString(bookingDto))
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", userDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.itemId", is(bookingDto.getItemId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingDto.getStart().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$.end", is(bookingDto.getEnd().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))));
    }

    @Test
    void getBookingTest() throws Exception {
        Mockito
                .when(bookingService.getBooking(anyLong(), anyLong()))
                .thenReturn(bookingDto);

        mvc.perform(get("/bookings/{bookingId}", bookingDto.getId())
                        .content(mapper.writeValueAsString(bookingDto))
                        .header("X-Sharer-User-Id", userDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.itemId", is(bookingDto.getItemId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingDto.getStart().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$.end", is(bookingDto.getEnd().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))));
    }

    @Test
    void getBookingWithWrongBookingIdTest() throws Exception {
        Mockito
                .when(bookingService.getBooking(anyLong(), anyLong()))
                .thenThrow(ValidationException.class);

        mvc.perform(get("/bookings/{bookingId}", -1)
                        .content(mapper.writeValueAsString(bookingDto))
                        .header("X-Sharer-User-Id", userDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    void getAllBookingsByBookerTest() throws Exception {
        Mockito
                .when(bookingService.getAllBookingsByBooker(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .header("X-Sharer-User-Id", userDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].itemId", is(bookingDto.getItemId()), Long.class))
                .andExpect(jsonPath("$[0].start", is(bookingDto.getStart().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$[0].end", is(bookingDto.getEnd().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))));
    }

    @Test
    void getAllBookingsByOwnerTest() throws Exception {
        Mockito
                .when(bookingService.getAllBookingsByOwner(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings/owner")
                        .content(mapper.writeValueAsString(bookingDto))
                        .header("X-Sharer-User-Id", userDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].itemId", is(bookingDto.getItemId()), Long.class))
                .andExpect(jsonPath("$[0].start", is(bookingDto.getStart().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$[0].end", is(bookingDto.getEnd().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))));
    }
}