package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.BookingStatus;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class BookingDto {

    @Data
    @AllArgsConstructor
    public static class Item {
        private int id;
        private String name;
    }

    @Data
    @AllArgsConstructor
    public static class User {
        private int id;
        private String name;
    }

    private Integer id;
    private LocalDate start;
    private LocalDate end;
    private BookingDto.Item item;
    private BookingDto.User booker;
    private BookingStatus status;
    private String feedback;

}
