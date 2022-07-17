package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.common.controller.RestAction;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@Validated
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @Validated({RestAction.Create.class})
    public BookingDto create(@RequestHeader("X-Sharer-User-Id") int userId,
                             @Valid @RequestBody BookingDto bookingDto) {
        return bookingService.create(userId, bookingDto);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable long id) {
        bookingService.delete(id);
    }

    @GetMapping(value = "/{id}")
    public BookingDto getItem(@RequestHeader("X-Sharer-User-Id") long bookerId,
                              @PathVariable long id) {
        return bookingService.getBookingById(bookerId, id);
    }

    // /bookings?state={state}
    @GetMapping
    public List<BookingDto> getBookingByBooker(@RequestHeader("X-Sharer-User-Id") long bookerId,
                                               @RequestParam(required = false, defaultValue = "ALL")
                                               BookingFilterState state) {
        return bookingService.getBookingByBooker(bookerId, state);
    }

    // /bookings/owner?state={state}
    @GetMapping(value = "/owner")
    public List<BookingDto> getBookingByOwner(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                              @RequestParam(required = false, defaultValue = "ALL")
                                              BookingFilterState state) {
        return bookingService.getBookingByOwner(ownerId, state);
    }

    // /{bookingId}?approved={approved}
    @PatchMapping(value = "/{id}")
    @Validated({RestAction.Update.class})
    public BookingDto changeStatus(@RequestHeader("X-Sharer-User-Id") long ownerId,
                             @PathVariable long id,
                             @RequestParam(required = true) Boolean approved) {

        return bookingService.changeStatus(ownerId, id, approved);

    }

}
