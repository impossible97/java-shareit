package ru.practicum.shareit.unitTest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;

@ExtendWith(MockitoExtension.class)
class BookingServiceUnitTest {

    @MockBean
    private ItemServiceImpl itemService;
    private static ItemDto item;
    private static UserDto owner;
    private static UserDto booker;

    @BeforeAll
    static void setMocksAndObjects() {
        item = new ItemDto();
        item.setName("Item1");
        item.setDescription("NewItem1");
        item.setAvailable(true);
    }
}
