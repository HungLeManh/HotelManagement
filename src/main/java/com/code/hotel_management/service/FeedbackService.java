package com.code.hotel_management.service;

import com.code.hotel_management.dto.request.FeedbackRequestDTO;
import com.code.hotel_management.dto.response.FeedbackDetailResponse;
import com.code.hotel_management.model.Feedback;

public interface FeedbackService {
    Feedback createFeedback(FeedbackRequestDTO feedbackRequest);

    FeedbackDetailResponse getFeedback(Long feedbackId);
}
