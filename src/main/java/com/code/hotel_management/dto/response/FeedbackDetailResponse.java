package com.code.hotel_management.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FeedbackDetailResponse {
    private Long feedbackid;
    private Long userid;
    private String name;
    private Long bookingid;
    private Long servicebookingid;
    private Integer rating;
    private String comments;
}
