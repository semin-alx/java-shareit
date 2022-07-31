package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import java.nio.charset.StandardCharsets;
import java.util.List;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ItemRequestControllerTest {

    @Mock
    ItemRequestService itemRequestService;

    @InjectMocks
    ItemRequestController itemRequestController;

    private final ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;

    @BeforeEach
    private void setup() {
        mvc = MockMvcBuilders.standaloneSetup(itemRequestController).build();
    }

    @Test
    public void createItemRequest() throws Exception {

        ItemRequestDto itemRequestDto1 = new ItemRequestDto(null, "фффф",
                null, null, null);

        when(itemRequestService.create(anyLong(), any()))
                .thenReturn(itemRequestDto1);

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemRequestDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto1.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto1.getDescription())));

    }

    @Test
    public void updateItemRequest() throws Exception {

        ItemRequestDto itemRequestDto1 = new ItemRequestDto(1L, "фффф",
                null, null, null);

        when(itemRequestService.update(anyLong(), anyLong(), any()))
                .thenReturn(itemRequestDto1);

        mvc.perform(patch("/requests/{id}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemRequestDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto1.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto1.getDescription())));

    }

    @Test
    public void deleteItemRequest() throws Exception {

        mvc.perform(delete("/requests/{id}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getItemRequest() throws Exception {

        ItemRequestDto itemRequestDto1 = new ItemRequestDto(1L, "фффф",
                null, null, null);

        when(itemRequestService.getItemRequestByOwnerId(anyLong(), anyLong()))
                .thenReturn(itemRequestDto1);

        mvc.perform(get("/requests/{id}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemRequestDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto1.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto1.getDescription())));

    }

    @Test
    public void getItemsAll() throws Exception {

        ItemRequestDto itemRequestDto1 = new ItemRequestDto(1L, "фффф",
                null, null, null);

        when(itemRequestService.getItemsAllExceptUserId(anyLong()))
                .thenReturn(List.of(itemRequestDto1));

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemRequestDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(itemRequestDto1))));

    }

}
