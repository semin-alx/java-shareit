package ru.practicum.shareit.mvc_tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {

    @Mock
    BookingService bookingService;

    @InjectMocks
    BookingController bookingController;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.standaloneSetup(bookingController).build();
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void createBooking() throws Exception {

        BookingDto bookingDto = new BookingDto(null, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), 1L, null, null, null);

        when(bookingService.create(anyLong(), any()))
                .thenReturn(bookingDto);

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.itemId", is(bookingDto.getItemId()), Long.class));

    }

    @Test
    public void updateBooking() throws Exception {

        BookingDto bookingDto = new BookingDto(1L, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), 1L, null, null, null);

        when(bookingService.changeStatus(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingDto);

        mvc.perform(patch("/bookings/{id}", 1)
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.itemId", is(bookingDto.getItemId()), Long.class));

    }

    @Test
    public void deleteBooking() throws Exception {

        mvc.perform(delete("/bookings/{id}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void getBookingById() throws Exception {

        BookingDto bookingDto = new BookingDto(1L, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), 1L, null, null, null);

        when(bookingService.getBookingById(anyLong(), anyLong()))
                .thenReturn(bookingDto);

        mvc.perform(get("/bookings/{id}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.itemId", is(bookingDto.getItemId()), Long.class));

    }

    @Test
    public void getBookingByOwner() throws Exception {

        BookingDto bookingDto = new BookingDto(1L, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), 1L, null, null, null);

        when(bookingService.getBookingByOwner(anyLong(), any()))
                .thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(bookingDto))));

    }

    @Test
    public void getBookingByBooker() throws Exception {

        BookingDto bookingDto = new BookingDto(1L, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), 1L, null, null, null);

        when(bookingService.getBookingByBooker(anyLong(), any()))
                .thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(bookingDto))));

    }

}
