package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class JsonBookingTest {

    @Autowired
    private JacksonTester<BookingDto> jacksonTester;

    @Test
    public void testDeserialization() throws IOException {

        String json = "{\"start\": \"2022-08-01T16:14:47\",\"end\": \"2022-08-02T16:14:47\",\"status\": \"WAITING\"}";

        LocalDateTime expectStart = LocalDateTime.of(2022, 8, 1, 16, 14, 47);
        LocalDateTime expectEnd = LocalDateTime.of(2022, 8, 2, 16, 14, 47);
        BookingStatus expectStatus = BookingStatus.WAITING;

        BookingDto bookingDto = jacksonTester.parseObject(json);

        Assertions.assertEquals(expectStart, bookingDto.getStart());
        Assertions.assertEquals(expectEnd, bookingDto.getEnd());
        Assertions.assertEquals(expectStatus, bookingDto.getStatus());

    }

    @Test
    public void testSerialization() throws IOException {

        BookingDto bookingDto = new BookingDto();
        bookingDto.setStart(LocalDateTime.of(2022, 8, 1, 16, 14, 47));
        bookingDto.setEnd(LocalDateTime.of(2022, 8, 2, 16, 14, 47));
        bookingDto.setStatus(BookingStatus.WAITING);

        JsonContent<BookingDto> json = jacksonTester.write(bookingDto);

        assertThat(json).extractingJsonPathStringValue("$.start").isEqualTo("2022-08-01T16:14:47");
        assertThat(json).extractingJsonPathStringValue("$.end").isEqualTo("2022-08-02T16:14:47");
        assertThat(json).extractingJsonPathStringValue("$.status").isEqualTo("WAITING");

    }


}
