package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.dto.BookingEntityDto;
import ru.practicum.shareit.booking.dto.BookingUpdateDto;
import ru.practicum.shareit.item.dto.ItemEntityDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

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
    public BookingEntityDto create(@RequestHeader("X-Sharer-User-Id") int userId,
                                   @Valid @RequestBody BookingCreationDto bookingDto) {
        return bookingService.create(userId, bookingDto);
    }

    @PatchMapping(value = "/{id}")
    public BookingEntityDto update(@RequestHeader("X-Sharer-User-Id") int bookerId,
                                @PathVariable int id,
                                @Valid @RequestBody BookingUpdateDto bookingDto) {
        return bookingService.update(bookerId, id, bookingDto);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable int id) {
        bookingService.delete(id);
    }

    @GetMapping
    public List<BookingEntityDto> getBookingAll() {
        return bookingService.getBookingAll();
    }

    @GetMapping(value = "/{id}")
    public BookingEntityDto getItem(@PathVariable int id) {
        return bookingService.getBookingById(id);
    }

}
