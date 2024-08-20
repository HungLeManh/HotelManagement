package com.code.hotel_management.service.impl;

import com.code.hotel_management.dto.response.ServiceBookingResponse;
import com.code.hotel_management.exception.ResourceNotFoundException;
import com.code.hotel_management.model.ServiceBooking;
import com.code.hotel_management.model.Services;
import com.code.hotel_management.model.User;
import com.code.hotel_management.repository.ServiceBookingRepository;
import com.code.hotel_management.repository.ServiceRepository;
import com.code.hotel_management.repository.UserRepository;
import com.code.hotel_management.service.ServiceBookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class ServiceBookingServiceImpl implements ServiceBookingService {

    private final ServiceBookingRepository serviceBookingRepository;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;
    @Override
    public ServiceBooking createServiceBooking(Long userId, Long serviceId, Integer quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Services service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));

        BigDecimal totalPrice = service.getPrice().multiply(BigDecimal.valueOf(quantity));

        ServiceBooking serviceBooking = ServiceBooking.builder()
                .user(user)
                .service(service)
                .quantity(quantity)
                .totalPrice(totalPrice)
                .paymentStatus("PENDING")
                .build();

        return serviceBookingRepository.save(serviceBooking);
    }

    @Override
    public ServiceBookingResponse getServiceBooking(Long serviceBookingId) {
        ServiceBooking serviceBooking = getServiceBookingById(serviceBookingId);
        return ServiceBookingResponse.builder()
                .serviceBookingId(serviceBooking.getServiceBookingId())
                .quantity(serviceBooking.getQuantity())
                .totalPrice(serviceBooking.getTotalPrice())
                .paymentStatus(serviceBooking.getPaymentStatus())
                .build();
    }

    @Override
    public void cancelServiceBooking(Long serviceBookingId) {
        ServiceBooking serviceBooking = getServiceBookingById(serviceBookingId);
        serviceBookingRepository.delete(serviceBooking);
    }

    public ServiceBooking getServiceBookingById(Long serviceBookingId) {
        return serviceBookingRepository.findById(serviceBookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Service booking not found"));
    }
}
