package com.code.hotel_management.service;

import com.code.hotel_management.dto.request.PaymentRequestDTO;
import com.code.hotel_management.dto.response.PaymentDetailResponse;
import com.code.hotel_management.model.Payment;

public interface PaymentService {
    Payment createPayment(PaymentRequestDTO paymentRequest);

    Payment getPayment(Long paymentId);

    boolean isEnoughMoney(PaymentRequestDTO paymentRequestDTO);

    PaymentDetailResponse paymentResponse(long paymentId);
}
