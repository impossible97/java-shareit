package ru.practicum.shareit.integrationTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class RequestServiceIntegrationTest {

    private final RequestService requestService;
    private final UserService userService;
    private static UserDto userDto;
    private static UserDto user;

    private static RequestDto requestDto;

    @BeforeAll
    static void setObjects() {
        userDto = new UserDto();
        userDto.setName("Name1");
        userDto.setEmail("email1@email.com");

        requestDto = new RequestDto();
        requestDto.setDescription("Description");
    }

    @BeforeEach
    void setRequestService() {
        user = userService.createUser(userDto);
    }

    @Test
    void addRequestWithWrongUserIdTest() {
        assertThrows(NotFoundException.class, () -> requestService.addRequest(99L, requestDto));
    }

    @Test
    void addRequestTest() {
        RequestDto request = requestService.addRequest(user.getId(), requestDto);

        assertThat(request, notNullValue());
        assertThat(request.getDescription(), equalTo(requestDto.getDescription()));
    }

    @Test
    void getRequestByIdWithWrongUserIdTest() {
        RequestDto request = requestService.addRequest(user.getId(), requestDto);

        assertThrows(NotFoundException.class, () -> requestService.getRequestById(request.getId(), 99L));
    }

    @Test
    void getRequestByIdWithWrongRequestIdTest() {
        requestService.addRequest(user.getId(), requestDto);

        assertThrows(NotFoundException.class, () -> requestService.getRequestById(99L, user.getId()));
    }

    @Test
    void getRequestByIdTest() {
        RequestDto request = requestService.addRequest(user.getId(), requestDto);

        RequestDto receivedRequest = requestService.getRequestById(request.getId(), user.getId());

        assertThat(receivedRequest, notNullValue());
        assertThat(receivedRequest, equalTo(request));
    }

    @Test
    void getRequestsWithWrongUserIdTest() {
        assertThrows(NotFoundException.class, () -> requestService.getRequests(99L));
    }

    @Test
    void getRequestsTest() {
        RequestDto request = requestService.addRequest(user.getId(), requestDto);

        List<RequestDto> requests = requestService.getRequests(user.getId());

        assertThat(requests, notNullValue());
        assertThat(requests, hasItem(request));
    }

    @Test
    void getAllRequestsWithWrongUserIdTest() {
        assertThrows(NotFoundException.class, () -> requestService.getAllRequests(0, 10, 99L));
    }

    @Test
    void getAllRequestsTest() {
        UserDto userDto1 = new UserDto();
        userDto1.setName("NewName");
        userDto1.setEmail("NewEmail@email.com");

        UserDto userServiceUser = userService.createUser(userDto1);
        RequestDto dto = new RequestDto();
        dto.setDescription("Something");

        RequestDto request1 = requestService.addRequest(user.getId(), requestDto);
        RequestDto request2 = requestService.addRequest(userServiceUser.getId(), dto);

        List<RequestDto> allRequests = requestService.getAllRequests(0, 10, userServiceUser.getId());

        assertThat(allRequests, notNullValue());
        assertThat(allRequests, hasItem(request1));
    }
}
