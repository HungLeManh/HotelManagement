package com.code.hotel_management.controller;

import com.code.hotel_management.dto.request.PaymentRequestDTO;
import com.code.hotel_management.dto.response.PaymentDetailResponse;
import com.code.hotel_management.dto.response.ResponseData;
import com.code.hotel_management.dto.response.ResponseError;
import com.code.hotel_management.dto.response.UserDetailResponse;
import com.code.hotel_management.exception.ResourceNotFoundException;
import com.code.hotel_management.model.Payment;
import com.code.hotel_management.service.PaymentService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@Validated
@Slf4j
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/")
    public ResponseEntity<Payment> createPayment(@RequestBody PaymentRequestDTO paymentRequest) {
        Payment payment = paymentService.createPayment(paymentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(payment);
    }

//    @GetMapping("/{paymentId}")
//    public ResponseEntity<Payment> getPayment(@PathVariable Long paymentId) {
//        Payment payment = paymentService.getPayment(paymentId);
//        return ResponseEntity.ok(payment);
//    }

    // get payment detail
    @GetMapping("/{paymentId}")
    public ResponseData<PaymentDetailResponse> paymentResponse(@PathVariable @Min(1) long paymentId) {
        log.info("Request get payment detail");
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "payment detail", paymentService.paymentResponse(paymentId));
        } catch (ResourceNotFoundException e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
