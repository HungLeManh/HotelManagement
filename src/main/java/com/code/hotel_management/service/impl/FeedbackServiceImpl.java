package com.code.hotel_management.service.impl;

import com.code.hotel_management.dto.request.FeedbackRequestDTO;
import com.code.hotel_management.dto.response.FeedbackDetailResponse;
import com.code.hotel_management.exception.ResourceNotFoundException;
import com.code.hotel_management.model.Booking;
import com.code.hotel_management.model.Feedback;
import com.code.hotel_management.model.ServiceBooking;
import com.code.hotel_management.model.User;
import com.code.hotel_management.repository.BookingRepository;
import com.code.hotel_management.repository.FeedbackRepository;
import com.code.hotel_management.repository.ServiceBookingRepository;
import com.code.hotel_management.repository.UserRepository;
import com.code.hotel_management.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final ServiceBookingRepository serviceBookingRepository;
    private final BookingRepository bookingRepository;

    @Override
    public Feedback createFeedback(FeedbackRequestDTO feedbackRequest) {
        User user = userRepository.findById(feedbackRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Feedback.FeedbackBuilder feedbackBuilder = Feedback.builder()
                .user(user)
                .rating(feedbackRequest.getRating())
                .comments(feedbackRequest.getComments());

        if (feedbackRequest.getServiceBookingId() != null) {
            ServiceBooking serviceBooking = serviceBookingRepository.findById(feedbackRequest.getServiceBookingId())
                    .orElseThrow(() -> new ResourceNotFoundException("Service booking not found"));
            feedbackBuilder.serviceBooking(serviceBooking);
        }

        if (feedbackRequest.getBookingId() != null) {
            Booking booking = bookingRepository.findById(feedbackRequest.getBookingId())
                    .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
            feedbackBuilder.booking(booking);
        }

        return feedbackRepository.save(feedbackBuilder.build());
    }

    @Override
    public FeedbackDetailResponse getFeedback(Long feedbackId) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new ResourceNotFoundException("feedback not found"));
        return FeedbackDetailResponse.builder()
                .feedbackid(feedback.getFeedbackid())
                .userid(feedback.getUser().getUserID())
                .name(feedback.getUser().getName())
                .bookingid(feedback.getBooking().getBookingId())
                .rating(feedback.getRating())
                .comments(feedback.getComments())
                .build();
    }
}
