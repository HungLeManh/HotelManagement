package com.code.hotel_management.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Builder
public class PaymentRequestDTO implements Serializable {
    @NotNull(message = "bookingId must be not null")
    private Long bookingId;

    @NotNull(message = "serviceBookingId must be not null")
    private Long serviceBookingId;

    @NotNull(message = "amount must be not null")
    private BigDecimal amount;

    @NotNull(message = "paymentMethod must be not null")
    private String paymentMethod;
}