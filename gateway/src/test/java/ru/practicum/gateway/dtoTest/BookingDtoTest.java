package ru.practicum.gateway.dtoTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.gateway.booking.dto.BookingDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingDtoTest {

    private final JacksonTester<BookingDto> json;

    @Test
    void testBookingDto() throws Exception {
        BookingDto bookingDto = new BookingDto();

        LocalDateTime start = LocalDateTime.of(2022, 9, 1, 12, 0, 0);
        LocalDateTime end = LocalDateTime.of(2022, 9, 1, 18, 0, 0);
        bookingDto.setStart(start);
        bookingDto.setEnd(end);
        bookingDto.setItemId(123L);

        JsonContent<BookingDto> result = json.write(bookingDto);

        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2022-09-01T12:00:00");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2022-09-01T18:00:00");
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(123);
    }
}
