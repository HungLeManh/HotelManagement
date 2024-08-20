package com.code.hotel_management.service;

import com.code.hotel_management.dto.response.BookingDetailResponse;
import com.itextpdf.text.DocumentException;

public interface PDFGeneratorService {
    byte[] generateBookingInvoice(BookingDetailResponse booking) throws DocumentException;

}
