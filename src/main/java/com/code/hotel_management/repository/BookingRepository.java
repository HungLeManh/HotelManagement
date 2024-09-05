package com.code.hotel_management.repository;

import com.code.hotel_management.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Date;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
//    @Query("SELECT DISTINCT b FROM Booking b JOIN b.rooms r WHERE r.id IN :roomIds " +
//            "AND ((b.checkindate BETWEEN :checkinDate AND :checkoutDate) " +
//            "OR (b.checkoutdate BETWEEN :checkinDate AND :checkoutDate))")
//    List<Booking> findOverlappingBookings(@Param("roomIds") List<Long> roomIds,
//                                          @Param("checkinDate") Date checkinDate,
//                                          @Param("checkoutDate") Date checkoutDate);
}