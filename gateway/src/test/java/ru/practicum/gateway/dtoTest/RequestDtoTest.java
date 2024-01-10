package ru.practicum.gateway.dtoTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.gateway.request.dto.RequestDto;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class RequestDtoTest {

    private final JacksonTester<RequestDto> json;

    @Test
    void testRequestDto() throws Exception {
        RequestDto requestDto = new RequestDto();

        requestDto.setDescription("Test description");
        requestDto.setCreated(LocalDateTime.now());

        JsonContent<RequestDto> result = json.write(requestDto);

        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Test description");
        assertThat(result).extractingJsonPathStringValue("$.created").isNotBlank();
    }
}
