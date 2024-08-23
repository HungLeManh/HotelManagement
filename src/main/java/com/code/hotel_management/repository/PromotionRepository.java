package com.code.hotel_management.repository;

import com.code.hotel_management.model.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Date;
import java.util.List;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    List<Promotion> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(Date date, Date sameDate);
}