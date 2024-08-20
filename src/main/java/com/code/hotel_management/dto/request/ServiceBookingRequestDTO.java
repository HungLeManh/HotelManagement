package com.code.hotel_management.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class ServiceBookingRequestDTO implements Serializable {
    @NotNull(message = "userId must be not null")
    private Long userId;

    @NotNull(message = "serviceId must be not null")
    private Long serviceId;

    @NotNull(message = "quantity must be not null")
    private Integer quantity;
}