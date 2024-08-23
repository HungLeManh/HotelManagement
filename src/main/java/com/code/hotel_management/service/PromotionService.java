package com.code.hotel_management.service;

import com.code.hotel_management.dto.request.PromotionRequestDTO;
import com.code.hotel_management.model.Promotion;

import java.util.Date;
import java.util.List;

public interface PromotionService {
    List<Promotion> findValidPromotions(Date bookingDate);

    Promotion createPromotion(PromotionRequestDTO requestDTO);
}
