package com.code.hotel_management.service.impl;

import com.code.hotel_management.dto.request.PromotionRequestDTO;
import com.code.hotel_management.model.Promotion;
import com.code.hotel_management.repository.PromotionRepository;
import com.code.hotel_management.service.PromotionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;

    @Override
    public List<Promotion> findValidPromotions(Date bookingDate) {
        return promotionRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(bookingDate, bookingDate);
    }

    @Override
    public Promotion createPromotion(PromotionRequestDTO requestDTO) {
        Promotion promotion = Promotion.builder()
                .promotionName(requestDTO.getPromotionName())
                .description(requestDTO.getDescription())
                .discountRate(requestDTO.getDiscountRate())
                .startDate(requestDTO.getStartDate())
                .endDate(requestDTO.getEndDate())
                .build();

        promotionRepository.save(promotion);

        return promotion;
    }
}
