package com.code.hotel_management.service;

import com.code.hotel_management.dto.response.BookingDetailResponse;
import com.code.hotel_management.model.Booking;
import com.code.hotel_management.model.Room;
import com.itextpdf.text.DocumentException;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface BookingService {
     Booking createBooking(Long userId, List<Long> roomIds, Date checkinDate, Date checkoutDate);

     boolean areRoomsAvailableAndEmpty(List<Room> rooms, Date checkinDate, Date checkoutDate);

     BigDecimal calculateTotalMoney(List<Room> rooms, Date checkinDate, Date checkoutDate);

     void checkoutBooking(Long bookingId);

     BookingDetailResponse getBookingDetail(Long bookingId);

     byte[] generateBookingInvoice(Long bookingId) throws IOException, DocumentException;
}