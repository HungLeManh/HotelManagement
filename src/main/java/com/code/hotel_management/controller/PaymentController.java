package com.code.hotel_management.controller;

import com.code.hotel_management.dto.request.PaymentRequestDTO;
import com.code.hotel_management.dto.response.PaymentDetailResponse;
import com.code.hotel_management.dto.response.ResponseData;
import com.code.hotel_management.dto.response.ResponseError;
import com.code.hotel_management.exception.ResourceNotFoundException;
import com.code.hotel_management.service.PaymentService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    public ResponseData<PaymentDetailResponse> createPayment(@RequestBody PaymentRequestDTO paymentRequest) {
        log.info("Request payment ");

        try {
            paymentService.createPayment(paymentRequest);
            if (paymentService.isEnoughMoney(paymentRequest)) {
                return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Payment success");
            }
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "not enough money");
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "request fail");
        }
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
