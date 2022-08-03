package ru.practicum.shareit.mvc_tests;

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
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import java.nio.charset.StandardCharsets;
import java.util.List;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {

    @Mock
    ItemService itemService;

    @InjectMocks
    ItemController itemController;

    private final ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;

    @BeforeEach
    private void setup() {
        mvc = MockMvcBuilders.standaloneSetup(itemController).build();
    }

    @Test
    public void createItem() throws Exception {

        ItemDto itemDto1 = new ItemDto(null,
                "Отвертка крестовая",
                "Почти новая", false, null, null, null, null,
                null);

        when(itemService.create(anyLong(), any()))
                .thenReturn(itemDto1);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto1.getName())));


    }

    @Test
    public void updateItem() throws Exception {

        ItemDto itemDto1 = new ItemDto(1L,
                "Отвертка крестовая",
                "Почти новая", false, null, null, null, null,
                null);

        when(itemService.update(anyLong(), anyLong(), any()))
                .thenReturn(itemDto1);

        mvc.perform(patch("/items/{id}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto1.getName())));

    }

    @Test
    public void deleteItem() throws Exception {

        mvc.perform(delete("/items/{id}", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void createComment() throws Exception {

        CommentDto commentDto = new CommentDto(null, "gggg", null, "",
                null);

        when(itemService.createComment(anyLong(), anyLong(), any()))
                .thenReturn(commentDto);

        mvc.perform(post("/items/{id}/comment", 1)
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentDto.getText())));

    }

    @Test
    public void getItem() throws Exception {

        ItemDto itemDto1 = new ItemDto(1L,
                "Отвертка крестовая",
                "Почти новая", false, null, null, null, null,
                null);

        when(itemService.getItemById(anyLong(), anyLong()))
                .thenReturn(itemDto1);

        mvc.perform(get("/items/{id}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto1.getName())));
    }

    @Test
    public void getCommentsByItem() throws Exception {

        CommentDto commentDto = new CommentDto(1L, "gggg", null, "",
                null);

        when(itemService.getCommentsByItem(anyLong()))
                .thenReturn(List.of(commentDto));

        mvc.perform(get("/items/{id}/comments", 1)
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(commentDto))));

    }

    @Test
    public void getItemsPage() throws Exception {

        ItemDto itemDto1 = new ItemDto(1L,
                "Отвертка крестовая",
                "Почти новая", false, null, null, null, null,
                null);

        when(itemService.getItems(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(itemDto1));

        mvc.perform(get("/items")
                        .param("from", "0")
                        .param("page", "20")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(itemDto1))));

    }

    @Test
    public void getItemsByText() throws Exception {

        ItemDto itemDto1 = new ItemDto(1L,
                "Отвертка крестовая",
                "Почти новая", false, null, null, null, null,
                null);

        when(itemService.findByText(anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(itemDto1));

        mvc.perform(get("/items/search")
                        .param("text", "Отвертка")
                        .param("from", "0")
                        .param("page", "20")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(itemDto1))));

    }

    @Test
    public void getCommentsByOwner() throws Exception {

        CommentDto commentDto = new CommentDto(1L, "gggg", null, "",
                null);

        when(itemService.getCommentsByOwner(anyLong()))
                .thenReturn(List.of(commentDto));

        mvc.perform(get("/items/comments")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(commentDto))));

    }

}
