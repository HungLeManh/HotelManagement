package com.code.hotel_management.repository;

import com.code.hotel_management.model.Services;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Services, Long> {}
