package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;
import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime start;

    @Column(name = "end_date_time", nullable = false)
    private LocalDateTime end;

    @ManyToOne(optional = false)
    @JoinColumn (name = "item_id")
    private Item item;

    @ManyToOne(optional = false)
    @JoinColumn (name = "booker_id")
    private User booker;

    @Column(name = "approved", nullable = false)
    private Boolean isApproved;

    @Column(name = "canceled", nullable = false)
    private Boolean isCanceled;

    public Booking() {

    }

}
