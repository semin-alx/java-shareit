package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Booking {
    private Integer id;
    private LocalDate start;
    private LocalDate end;
    private Item item;
    private User booker;
    private BookingStatus status;
    private String feedback;
}
