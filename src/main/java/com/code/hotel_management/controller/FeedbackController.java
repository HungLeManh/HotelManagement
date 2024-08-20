package com.code.hotel_management.controller;

import com.code.hotel_management.dto.request.FeedbackRequestDTO;
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
    public ResponseEntity<Feedback> createFeedback(@RequestBody FeedbackRequestDTO feedbackRequest) {
        Feedback feedback = feedbackService.createFeedback(feedbackRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(feedback);
    }

    @GetMapping("/{feedbackId}")
    public ResponseEntity<Feedback> getFeedback(@PathVariable Long feedbackId) {
        Feedback feedback = feedbackService.getFeedback(feedbackId);
        return ResponseEntity.ok(feedback);
    }
}
