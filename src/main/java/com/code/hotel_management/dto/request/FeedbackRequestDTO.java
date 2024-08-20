package com.code.hotel_management.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class FeedbackRequestDTO implements Serializable {
    @NotNull(message = "userId must be not null")
    private Long userId;
    @NotNull(message = "serviceBookingId must be not null")
    private Long serviceBookingId;
    @NotNull(message = "bookingId must be not null")
    private Long bookingId;
    @NotNull(message = "rating must be not null")
    private Integer rating;
    @NotNull(message = "comments must be not null")
    private String comments;
}
