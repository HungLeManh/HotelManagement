package com.code.hotel_management.repository;

import com.code.hotel_management.model.ServiceBooking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceBookingRepository extends JpaRepository<ServiceBooking, Long> {}