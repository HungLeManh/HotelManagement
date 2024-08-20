package com.code.hotel_management.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Builder
public class PaymentDetailResponse implements Serializable {
    @NotNull(message = "paymentId must be not null")
    private Long paymentId;

    @NotNull(message = "bookingId must be not null")
    private Long bookingId;

    @NotNull(message = "serviceBookingId must be not null")
    private Long serviceBookingId;

    @NotBlank(message = "name must be not blank") // Khong cho phep gia tri blank
    private String name;

    @NotNull(message = "amount must be not null")
    private BigDecimal amount;

    @NotNull(message = "paymentMethod must be not null")
    private String paymentMethod;

    @NotNull(message = "paymentDate must be not null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date paymentDate;
}
