package com.code.hotel_management.controller;

import com.code.hotel_management.dto.request.BookingRequestDTO;
import com.code.hotel_management.dto.response.BookingDetailResponse;
import com.code.hotel_management.dto.response.ResponseData;
import com.code.hotel_management.dto.response.ResponseError;
import com.code.hotel_management.dto.response.UserDetailResponse;
import com.code.hotel_management.exception.ResourceNotFoundException;
import com.code.hotel_management.model.Booking;
import com.code.hotel_management.service.BookingService;
import com.itextpdf.text.DocumentException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/booking")
@Validated
@Slf4j
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    // booking
    @PostMapping("/")
    public ResponseData<?> createBooking(@Valid @RequestBody BookingRequestDTO bookingRequest) {
        log.info("Request booking");

        try {
            Booking booking = bookingService.createBooking(bookingRequest);
            return new ResponseData<>(HttpStatus.CREATED.value(), "booking success");
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "One or more rooms are not available");
        }
    }

    // get booking detail
    @GetMapping("/{bookingId}")
    public ResponseData<BookingDetailResponse> getUser(@PathVariable @Min(1) long bookingId) {
        log.info("Request get booking detail");
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "booking detail", bookingService.getBookingDetail(bookingId));
        } catch (ResourceNotFoundException e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    // checkout booking
    @PostMapping("/{bookingId}/checkout")
    public ResponseData<Void> checkoutBooking(@PathVariable Long bookingId) {
        bookingService.checkoutBooking(bookingId);
        return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "checkout booking successfully");
    }

    // get invoice
    @GetMapping("/{bookingId}/invoice")
    public ResponseEntity<byte[]> generateInvoice(@PathVariable Long bookingId) throws IOException, DocumentException {
        byte[] pdfBytes = bookingService.generateBookingInvoice(bookingId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "booking_invoice_" + bookingId + ".pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}
