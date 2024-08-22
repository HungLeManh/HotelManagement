package com.code.hotel_management.repository;

import com.code.hotel_management.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("SELECT COUNT(r) FROM Room r WHERE r.roomid IN :roomids " +
            "AND r.status = 'EMPTY' " +
            "AND NOT EXISTS (SELECT b FROM Booking b JOIN b.rooms br " +
            "                WHERE br.roomid = r.roomid " +
            "                AND (b.checkindate < :checkoutdate AND b.checkoutdate > :checkindate))")

    long countAvailableRooms(@Param("roomids") List<Long> roomids,
                             @Param("checkindate") Date checkindate,
                             @Param("checkoutdate") Date checkoutdate);
}