package com.code.hotel_management.controller;

import com.code.hotel_management.dto.request.BookingRequestDTO;
import com.code.hotel_management.dto.request.PaymentRequestDTO;
import com.code.hotel_management.dto.request.PromotionRequestDTO;
import com.code.hotel_management.dto.response.ResponseData;
import com.code.hotel_management.dto.response.ResponseError;
import com.code.hotel_management.model.Booking;
import com.code.hotel_management.model.Promotion;
import com.code.hotel_management.service.PromotionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/promotion")
@Validated
@Slf4j
@RequiredArgsConstructor
public class PromotionController {
    private final PromotionService promotionService;

    // add promotion
    @PostMapping("/")
    public ResponseData<?> createPromotion(@Valid @RequestBody PromotionRequestDTO requestDTO) {
        log.info("Adding new promotion");

        try {
            Promotion promotion = promotionService.createPromotion(requestDTO);
            return new ResponseData<>(HttpStatus.CREATED.value(), "adding promotion success");
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "adding promotion fail");
        }
    }
}
