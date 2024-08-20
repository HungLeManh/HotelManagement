package com.code.hotel_management.repository;

import com.code.hotel_management.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {}