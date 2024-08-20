package com.code.hotel_management.controller;

import com.code.hotel_management.dto.request.ServiceBookingRequestDTO;
import com.code.hotel_management.dto.response.*;
import com.code.hotel_management.exception.ResourceNotFoundException;
import com.code.hotel_management.model.ServiceBooking;
import com.code.hotel_management.service.ServiceBookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/service")
@Validated
@Slf4j
@RequiredArgsConstructor
public class ServiceBookingController {
    private final ServiceBookingService serviceBookingService;

    // booking service
    @PostMapping("/")
    public ResponseData<ServiceBooking> createServiceBooking(@RequestBody ServiceBookingRequestDTO serviceBookingRequest) {
        ServiceBooking serviceBooking = serviceBookingService.createServiceBooking(
                serviceBookingRequest.getUserId(),
                serviceBookingRequest.getServiceId(),
                serviceBookingRequest.getQuantity()
        );
        return new ResponseData<ServiceBooking>(HttpStatus.CREATED.value(), "booking success");
    }

    // get service
    @GetMapping("/{serviceBookingId}")
    public ResponseData<ServiceBookingResponse> getServiceBooking(@PathVariable Long serviceBookingId) {
        log.info("Request get service detail");
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "user", serviceBookingService.getServiceBooking(serviceBookingId));
        } catch (ResourceNotFoundException e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    // delete service
    @DeleteMapping("/{serviceBookingId}")
    public ResponseData<Void> cancelServiceBooking(@PathVariable Long serviceBookingId) {
        serviceBookingService.cancelServiceBooking(serviceBookingId);
        return new ResponseData<>(HttpStatus.CREATED.value(), "delete success");
    }
}