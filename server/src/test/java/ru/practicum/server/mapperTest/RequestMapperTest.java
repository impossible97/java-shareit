package ru.practicum.server.mapperTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.item.model.ItemRequestProjection;
import ru.practicum.server.mapper.RequestMapper;
import ru.practicum.server.request.dto.RequestDto;
import ru.practicum.server.request.model.ItemRequest;
import ru.practicum.server.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

class RequestMapperTest {

    private static final RequestMapper requestMapper = new RequestMapper();

    private ItemRequest request;
    private RequestDto requestDto;
    private User user;
    private Item item;
    private ItemRequestProjection itemRequestProjection;

    @BeforeEach
    void setObjects() {
        user = new User();
        user.setId(1L);
        user.setName("Name");
        user.setEmail("email@email.com");

        request = new ItemRequest();
        request.setId(1L);
        request.setDescription("Desscription");
        request.setCreated(LocalDateTime.now());
        request.setUser(user);

        item = new Item();
        item.setId(1L);
        item.setName("Item");
        item.setDescription("Description");
        item.setAvailable(true);
        item.setRequest(request);
        item.setOwner(user);

        itemRequestProjection = new ItemRequestProjection() {
            @Override
            public Long getId() {
                return item.getId();
            }

            @Override
            public String getName() {
                return item.getName();
            }

            @Override
            public String getDescription() {
                return item.getName();
            }

            @Override
            public Boolean getAvailable() {
                return item.getAvailable();
            }

            @Override
            public long getRequestId() {
                return item.getRequest().getId();
            }
        };

        requestDto = new RequestDto();
        requestDto.setId(request.getId());
        requestDto.setDescription(request.getDescription());
        requestDto.setCreated(request.getCreated());
        requestDto.setItems(List.of(itemRequestProjection));
    }

    @Test
    void toDtoTest() {
        RequestDto dto = requestMapper.toDto(request, List.of(itemRequestProjection));

        assertThat(dto, notNullValue());
        assertThat(dto, equalTo(requestDto));
    }

    @Test
    void toEntityTest() {
        ItemRequest entity = requestMapper.toEntity(requestDto, user);

        assertThat(entity, notNullValue());
        assertThat(entity.getDescription(), equalTo(request.getDescription()));
        assertThat(entity.getUser(), equalTo(request.getUser()));
    }
}
