package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByItemId(long itemId);

    @Query(value = "SELECT * FROM bookings " +
                   "WHERE booker_id = :bookerId " +
                   "AND NOT canceled " +
                   "ORDER BY start_date_time DESC",
           nativeQuery = true)
    List<Booking> findByBookerId(long bookerId);

    @Query(value = "SELECT * FROM bookings " +
            "WHERE booker_id = :bookerId " +
            "AND NOT canceled " +
            "ORDER BY start_date_time DESC /*:pageable*/",
            nativeQuery = true)
    Page<Booking> findByBookerId(long bookerId, Pageable pageable);

    @Query(value = "SELECT bookings.* FROM bookings " +
                   "INNER JOIN items ON items.id = bookings.item_id " +
                   "WHERE items.owner_id = :ownerId " +
                   "ORDER BY start_date_time DESC ",
           nativeQuery = true)
    List<Booking> findByOwnerId(long ownerId);

    @Query(value = "SELECT bookings.* FROM bookings " +
            "INNER JOIN items ON items.id = bookings.item_id " +
            "WHERE items.owner_id = :ownerId " +
            "ORDER BY start_date_time DESC /*:pageable*/",
            nativeQuery = true)
    Page<Booking> findByOwnerId(long ownerId, Pageable pageable);

    @Query(value = "SELECT * FROM bookings WHERE booker_id = :bookerId " +
            "AND start_date_time <= :time " +
                   "AND end_date_time >= :time",
           nativeQuery = true)
    List<Booking> findCurrentByBooker(long bookerId, LocalDateTime time);

    @Query(value = "SELECT * FROM bookings WHERE booker_id = :bookerId " +
            "AND start_date_time <= :time " +
            "AND end_date_time >= :time /*:pageable*/",
            nativeQuery = true)
    Page<Booking> findCurrentByBooker(long bookerId, LocalDateTime time, Pageable pageable);

    @Query(value = "SELECT * FROM bookings WHERE booker_id = :bookerId " +
            "AND end_date_time < :time",
            nativeQuery = true)
    List<Booking> findPastByBooker(long bookerId, LocalDateTime time);

    @Query(value = "SELECT * FROM bookings WHERE booker_id = :bookerId " +
            "AND end_date_time < :time /*:pageable*/",
            nativeQuery = true)
    Page<Booking> findPastByBooker(long bookerId, LocalDateTime time, Pageable pageable);

    @Query(value = "SELECT * FROM bookings WHERE booker_id = :bookerId " +
            "AND start_date_time > :time ORDER BY start_date_time DESC",
            nativeQuery = true)
    List<Booking> findFutureByBooker(long bookerId, LocalDateTime time);

    @Query(value = "SELECT * FROM bookings WHERE booker_id = :bookerId " +
            "AND start_date_time > :time ORDER BY start_date_time DESC /*:pageable*/",
            nativeQuery = true)
    Page<Booking> findFutureByBooker(long bookerId, LocalDateTime time, Pageable pageable);

    @Query(value = "SELECT * FROM bookings WHERE booker_id = :bookerId " +
            "AND (approved = :approved) AND (canceled = :canceled)",
            nativeQuery = true)
    List<Booking> findByStatusByBooker(long bookerId, boolean approved, boolean canceled);

    @Query(value = "SELECT * FROM bookings WHERE booker_id = :bookerId " +
            "AND (approved = :approved) AND (canceled = :canceled) /*:pageable*/",
            nativeQuery = true)
    Page<Booking> findByStatusByBooker(long bookerId, boolean approved, boolean canceled,
                                       Pageable pageable);

    @Query(value = "SELECT bookings.* FROM bookings " +
                   "INNER JOIN items ON items.id = bookings.item_id " +
                   "WHERE items.owner_id = :ownerId " +
                   "AND start_date_time <= :time " +
                   "AND end_date_time >= :time",
            nativeQuery = true)
    List<Booking> findCurrentByOwner(long ownerId, LocalDateTime time);

    @Query(value = "SELECT bookings.* FROM bookings " +
            "INNER JOIN items ON items.id = bookings.item_id " +
            "WHERE items.owner_id = :ownerId " +
            "AND start_date_time <= :time " +
            "AND end_date_time >= :time /*:pageable*/",
            nativeQuery = true)
    Page<Booking> findCurrentByOwner(long ownerId, LocalDateTime time, Pageable pageable);

    @Query(value = "SELECT bookings.* FROM bookings " +
                   "INNER JOIN items ON items.id = bookings.item_id " +
                   "WHERE items.owner_id = :ownerId " +
                   "AND end_date_time < :time",
           nativeQuery = true)
    List<Booking> findPastByOwner(long ownerId, LocalDateTime time);

    @Query(value = "SELECT bookings.* FROM bookings " +
            "INNER JOIN items ON items.id = bookings.item_id " +
            "WHERE items.owner_id = :ownerId " +
            "AND end_date_time < :time /*:pageable*/",
            nativeQuery = true)
    Page<Booking> findPastByOwner(long ownerId, LocalDateTime time, Pageable pageable);

    @Query(value = "SELECT bookings.* FROM bookings " +
                   "INNER JOIN items ON items.id = bookings.item_id " +
                   "WHERE items.owner_id = :ownerId " +
                   "AND start_date_time > :time " +
                   "ORDER BY start_date_time DESC",
            nativeQuery = true)
    List<Booking> findFutureByOwner(long ownerId, LocalDateTime time);

    @Query(value = "SELECT bookings.* FROM bookings " +
            "INNER JOIN items ON items.id = bookings.item_id " +
            "WHERE items.owner_id = :ownerId " +
            "AND start_date_time > :time " +
            "ORDER BY start_date_time DESC /*:pageable*/",
            nativeQuery = true)
    Page<Booking> findFutureByOwner(long ownerId, LocalDateTime time, Pageable pageable);

    @Query(value = "SELECT bookings.* FROM bookings " +
            "INNER JOIN items ON items.id = bookings.item_id " +
            "WHERE items.owner_id = :ownerId " +
            "AND (approved = :approved) AND (canceled = :canceled) ",
            nativeQuery = true)
    List<Booking> findByStatusByOwner(long ownerId, boolean approved, boolean canceled);

    @Query(value = "SELECT bookings.* FROM bookings " +
            "INNER JOIN items ON items.id = bookings.item_id " +
            "WHERE items.owner_id = :ownerId " +
            "AND (approved = :approved) AND (canceled = :canceled) /*:pageable*/",
            nativeQuery = true)
    Page<Booking> findByStatusByOwner(long ownerId, boolean approved, boolean canceled,
                                      Pageable pageable);

    @Query(value = "SELECT * FROM bookings WHERE item_id = :itemId AND end_date_time < :time " +
                   " ORDER BY end_date_time DESC LIMIT 1",
           nativeQuery = true)
    Optional<Booking> getLastBooking(long itemId, LocalDateTime time);

    @Query(value = "SELECT * FROM bookings WHERE item_id = :itemId AND start_date_time > :time " +
            " ORDER BY start_date_time LIMIT 1",
            nativeQuery = true)
    Optional<Booking> getFutureBooking(long itemId, LocalDateTime time);

}
