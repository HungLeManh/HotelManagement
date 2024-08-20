package com.code.hotel_management.service.impl;

import com.code.hotel_management.dto.request.PaymentRequestDTO;
import com.code.hotel_management.dto.response.PaymentDetailResponse;
import com.code.hotel_management.exception.ResourceNotFoundException;
import com.code.hotel_management.model.Booking;
import com.code.hotel_management.model.Payment;
import com.code.hotel_management.model.ServiceBooking;
import com.code.hotel_management.model.User;
import com.code.hotel_management.repository.BookingRepository;
import com.code.hotel_management.repository.PaymentRepository;
import com.code.hotel_management.repository.ServiceBookingRepository;
import com.code.hotel_management.repository.UserRepository;
import com.code.hotel_management.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final ServiceBookingRepository serviceBookingRepository;
    private final UserRepository userRepository;

    @Override
    public Payment createPayment(PaymentRequestDTO paymentRequest) {
        Payment.PaymentBuilder paymentBuilder = Payment.builder()
                .amount(paymentRequest.getAmount())
                .paymentMethod(paymentRequest.getPaymentMethod())
                .paymentDate(new Date());

        if (paymentRequest.getBookingId() != null && isEnoughMoney(paymentRequest)) {
            Booking booking = bookingRepository.findById(paymentRequest.getBookingId())
                    .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
            paymentBuilder.booking(booking);
            booking.setPaymentstatus("PAID");
            bookingRepository.save(booking);
        }

        if (paymentRequest.getServiceBookingId() != null && isEnoughMoney(paymentRequest)) {
            ServiceBooking serviceBooking = serviceBookingRepository.findById(paymentRequest.getServiceBookingId())
                    .orElseThrow(() -> new ResourceNotFoundException("Service booking not found"));
            paymentBuilder.serviceBooking(serviceBooking);
            // Assume ServiceBooking has a paymentStatus field
            serviceBooking.setPaymentStatus("PAID");
            serviceBookingRepository.save(serviceBooking);
        }

        return paymentRepository.save(paymentBuilder.build());
    }

    @Override
    public Payment getPayment(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
    }

    @Override
    public boolean isEnoughMoney(PaymentRequestDTO paymentRequestDTO){
        if(paymentRequestDTO.getBookingId()!=null){
            Booking booking = bookingRepository.findById(paymentRequestDTO.getBookingId())
                    .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
            if (paymentRequestDTO.getAmount().compareTo(booking.getTotalmoney()) >= 0){
                return true;
            }
        }
        if(paymentRequestDTO.getServiceBookingId()!=null){
            ServiceBooking serviceBooking = serviceBookingRepository.findById(paymentRequestDTO.getServiceBookingId())
                    .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
            if (paymentRequestDTO.getAmount().compareTo(serviceBooking.getTotalPrice()) >= 0){
                return true;
            }
        }
        return false;
    }

    @Override
    public PaymentDetailResponse paymentResponse(long paymentId) {
        Payment payment = getPaymentById(paymentId);
        if(payment.getBooking().getBookingId() != null){
            Booking booking = bookingRepository.findById(payment.getBooking().getBookingId())
                    .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
            User user = userRepository.findById(payment.getBooking().getUser().getUserID())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            return PaymentDetailResponse.builder()
                    .paymentId(paymentId)
                    .bookingId(booking.getBookingId())
                    .name(user.getName())
                    .amount(payment.getAmount())
                    .paymentMethod(payment.getPaymentMethod())
                    .paymentDate(payment.getPaymentDate())
                    .build();
        }
        if(payment.getServiceBooking().getServiceBookingId() != null){
            ServiceBooking serviceBooking = serviceBookingRepository.findById(payment.getServiceBooking().getServiceBookingId())
                    .orElseThrow(() -> new ResourceNotFoundException("Service Booking not found"));
            User user = userRepository.findById(payment.getBooking().getUser().getUserID())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            return PaymentDetailResponse.builder()
                    .paymentId(paymentId)
                    .serviceBookingId(serviceBooking.getServiceBookingId())
                    .name(user.getName())
                    .amount(payment.getAmount())
                    .paymentMethod(payment.getPaymentMethod())
                    .paymentDate(payment.getPaymentDate())
                    .build();
        }
        return PaymentDetailResponse.builder().build();
    }


    public Payment getPaymentById(long paymentId){
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
    }
}
