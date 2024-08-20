package com.code.hotel_management.service;

import com.code.hotel_management.dto.response.ServiceBookingResponse;
import com.code.hotel_management.model.ServiceBooking;

public interface ServiceBookingService {
    ServiceBooking createServiceBooking(Long userId, Long serviceId, Integer quantity);

    ServiceBookingResponse getServiceBooking(Long serviceBookingId);

    void cancelServiceBooking(Long serviceBookingId);
}
