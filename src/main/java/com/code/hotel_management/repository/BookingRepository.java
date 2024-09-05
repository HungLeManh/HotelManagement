package com.code.hotel_management.repository;

import com.code.hotel_management.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Date;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

}