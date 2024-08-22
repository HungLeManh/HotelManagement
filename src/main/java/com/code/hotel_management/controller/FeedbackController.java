package com.code.hotel_management.controller;

import com.code.hotel_management.dto.request.FeedbackRequestDTO;
import com.code.hotel_management.dto.response.FeedbackDetailResponse;
import com.code.hotel_management.dto.response.ResponseData;
import com.code.hotel_management.dto.response.ResponseError;
import com.code.hotel_management.exception.ResourceNotFoundException;
import com.code.hotel_management.model.Feedback;
import com.code.hotel_management.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedback")
@Validated
@Slf4j
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;

    @PostMapping("/")
    public ResponseData<?> createFeedback(@RequestBody FeedbackRequestDTO feedbackRequest) {

        log.info("send feedback");

        try {
            Feedback feedback = feedbackService.createFeedback(feedbackRequest);
            return new ResponseData<>(HttpStatus.CREATED.value(), "feedback send success");

        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "feedback send fail");
        }
    }

    @GetMapping("/{feedbackId}")
    public ResponseData<FeedbackDetailResponse> getFeedback(@PathVariable Long feedbackId) {
        log.info("Request get feedback detail");
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "feedback detail", feedbackService.getFeedback(feedbackId));
        } catch (ResourceNotFoundException e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
