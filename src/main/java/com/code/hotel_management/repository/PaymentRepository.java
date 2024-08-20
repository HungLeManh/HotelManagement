package com.code.hotel_management.repository;

import com.code.hotel_management.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {}
