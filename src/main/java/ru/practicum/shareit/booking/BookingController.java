package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingUpdateDto;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDto create(@RequestHeader("X-Sharer-User-Id") int userId,
                             @Valid @RequestBody BookingCreationDto bookingDto) {
        return bookingService.create(userId, bookingDto);
    }

    @PatchMapping(value = "/{id}")
    public BookingDto update(@RequestHeader("X-Sharer-User-Id") int bookerId,
                             @PathVariable int id,
                             @Valid @RequestBody BookingUpdateDto bookingDto) {
        return bookingService.update(bookerId, id, bookingDto);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable int id) {
        bookingService.delete(id);
    }

    @GetMapping
    public List<BookingDto> getBookingAll() {
        return bookingService.getBookingAll();
    }

    @GetMapping(value = "/{id}")
    public BookingDto getItem(@PathVariable int id) {
        return bookingService.getBookingById(id);
    }

}
