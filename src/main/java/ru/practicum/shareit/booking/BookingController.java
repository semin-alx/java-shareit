package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.common.controller.RestAction;
import ru.practicum.shareit.common.error_handling.exception.InvalidRequestHeaderException;
import ru.practicum.shareit.common.error_handling.exception.InvalidRequestParamException;

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
    public BookingDto create(@RequestHeader("X-Sharer-User-Id") long userId,
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

    // /bookings?state={state}&from=0&page=0
    @GetMapping
    public List<BookingDto> getBookingByBooker(
            @RequestHeader("X-Sharer-User-Id") long bookerId,
            @RequestParam(required = false, defaultValue = "ALL") String state,
            @RequestParam(required = false) Integer from,
            @RequestParam(required = false) Integer page) {

        BookingFilterState stateA;

        try {
            stateA = BookingFilterState.parse(state);
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestHeaderException("Unknown state: " + state);
        }

        if ((from == null) && (page == null)) {
            return bookingService.getBookingByBooker(bookerId, stateA);
        } else if ((from != null) && (page != null) && (from >= 0) && (page > 0)) {
            return bookingService.getBookingByBooker(bookerId, stateA, from, page);
        } else {
            throw new InvalidRequestParamException("Неверные параметры from или page");
        }

    }

    // /bookings/owner?state={state}&from=0&page=0
    @GetMapping(value = "/owner")
    public List<BookingDto> getBookingByOwner(
            @RequestHeader("X-Sharer-User-Id") long ownerId,
            @RequestParam(required = false, defaultValue = "ALL") String state,
            @RequestParam(required = false) Integer from,
            @RequestParam(required = false) Integer page) {

        BookingFilterState stateA;

        try {
            stateA = BookingFilterState.parse(state);
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestHeaderException("Unknown state: " + state);
        }

        if ((from == null) && (page == null)) {
            return bookingService.getBookingByOwner(ownerId, stateA);
        } else if ((from != null) && (page != null) && (from >= 0) && (page > 0)) {
            return bookingService.getBookingByOwner(ownerId, stateA, from, page);
        } else {
            throw new InvalidRequestParamException("Неверные параметры from или page");
        }

    }

    // /{bookingId}?approved={approved}
    @PatchMapping(value = "/{id}")
    @Validated({RestAction.Update.class})
    public BookingDto changeStatus(@RequestHeader("X-Sharer-User-Id") long userId,
                             @PathVariable long id,
                             @RequestParam(required = true) Boolean approved) {

        return bookingService.changeStatus(userId, id, approved);

    }

}
