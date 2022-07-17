package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByItemId(long itemId);

    List<Booking> findByBookerId(long bookerId);

    @Query(value = "SELECT bookings.* FROM bookings " +
                   "INNER JOIN items ON items.id = bookings.item_id " +
                   "WHERE items.owner_id = :ownerId",
           nativeQuery = true)
    List<Booking> findByOwnerId(long ownerId);

    @Query(value = "SELECT * FROM booking WHERE booker_id = :bookerId " +
                   "AND start_date_time <= :time " +
                   "AND end_date_time >= :time",
           nativeQuery = true)
    List<Booking> findCurrentByBooker(long bookerId, LocalDateTime time);

    @Query(value = "SELECT * FROM booking WHERE booker_id = :bookerId " +
            "AND end_date_time < :time",
            nativeQuery = true)
    List<Booking> findPastByBooker(long bookerId, LocalDateTime time);

    @Query(value = "SELECT * FROM booking WHERE booker_id = :bookerId " +
            "AND start_date_time > :time",
            nativeQuery = true)
    List<Booking> findFutureByBooker(long bookerId, LocalDateTime time);

    @Query(value = "SELECT * FROM booking WHERE booker_id = :bookerId " +
            "AND (approved = :approved) AND (canceled = :canceled)",
            nativeQuery = true)
    List<Booking> findByStatusByBooker(long bookerId, boolean approved, boolean canceled);

    @Query(value = "SELECT booking.* FROM booking " +
                   "INNER JOIN items ON items.id = booking.item_id " +
                   "WHERE items.owner_id = :ownerId " +
                   "AND start_date_time <= :time " +
                   "AND end_date_time >= :time",
            nativeQuery = true)
    List<Booking> findCurrentByOwner(long ownerId, LocalDateTime time);

    @Query(value = "SELECT booking.* FROM booking " +
                   "INNER JOIN items ON items.id = booking.item_id " +
                   "WHERE items.owner_id = :ownerId " +
                   "AND end_date_time < :time",
           nativeQuery = true)
    List<Booking> findPastByOwner(long ownerId, LocalDateTime time);

    @Query(value = "SELECT booking.* FROM booking " +
                   "INNER JOIN items ON items.id = booking.item_id " +
                   "WHERE items.owner_id = :ownerId " +
                   "AND start_date_time > :time",
            nativeQuery = true)
    List<Booking> findFutureByOwner(long ownerId, LocalDateTime time);

    @Query(value = "SELECT booking.* FROM booking " +
            "INNER JOIN items ON items.id = booking.item_id " +
            "WHERE items.owner_id = :ownerId " +
            "AND (approved = :approved) AND (canceled = :canceled)",
            nativeQuery = true)
    List<Booking> findByStatusByOwner(long ownerId, boolean approved, boolean canceled);

}
