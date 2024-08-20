package com.code.hotel_management.dto.response;

import com.code.hotel_management.model.Services;
import com.code.hotel_management.model.User;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Builder
public class ServiceBookingResponse implements Serializable {
    private Long serviceBookingId;
//    private User user;
//    private Services service;
    private Integer quantity;
    private BigDecimal totalPrice;
    private String paymentStatus;
}
