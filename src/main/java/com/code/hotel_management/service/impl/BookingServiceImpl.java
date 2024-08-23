package com.code.hotel_management.service.impl;

import com.code.hotel_management.dto.request.BookingRequestDTO;
import com.code.hotel_management.dto.response.BookingDetailResponse;
import com.code.hotel_management.exception.ResourceNotFoundException;
import com.code.hotel_management.model.Booking;
import com.code.hotel_management.model.Room;
import com.code.hotel_management.model.User;
import com.code.hotel_management.repository.BookingRepository;
import com.code.hotel_management.repository.RoomRepository;
import com.code.hotel_management.repository.UserRepository;
import com.code.hotel_management.service.BookingService;
import com.code.hotel_management.service.PDFGeneratorService;
import com.code.hotel_management.util.RoomStatus;
import com.itextpdf.text.DocumentException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final EntityManager entityManager;
    @Override
    @Transactional
    public Booking createBooking(BookingRequestDTO requestDTO) {
        Long userId = requestDTO.getUserId();
        List<Long> roomIds = requestDTO.getRoomIds();
        Date checkinDate =requestDTO.getCheckinDate();
        Date checkoutDate = requestDTO.getCheckoutDate();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Room> rooms = roomRepository.findAllById(roomIds);

        // Check if rooms are available
        if (!areRoomsAvailableAndEmpty(roomIds, checkinDate, checkoutDate)) {
            throw new RuntimeException("One or more rooms are not available for the selected dates.");
        }

        // Calculate total money
        BigDecimal totalMoney = calculateTotalMoney(rooms, checkinDate, checkoutDate);

        // Call stored procedure
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("create_booking")
                .registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_room_ids", Long[].class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_checkin_date", Date.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_checkout_date", Date.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_total_money", BigDecimal.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_booking_id", Long.class, ParameterMode.OUT)
                .setParameter("p_user_id", userId)
                .setParameter("p_room_ids", roomIds.toArray(new Long[0]))
                .setParameter("p_checkin_date", checkinDate)
                .setParameter("p_checkout_date", checkoutDate)
                .setParameter("p_total_money", totalMoney);
        query.execute();

        Long bookingId = (Long) query.getOutputParameterValue("p_booking_id");

        Booking booking = Booking.builder()
                .user(user)
                .rooms(rooms)
                .checkindate(checkinDate)
                .checkoutdate(checkoutDate)
                .bookingdate(new Date())
                .totalmoney(totalMoney)
                .paymentstatus("PENDING")
                .build();

        // Cập nhật bookingId cho các phòng
//        Booking finalBooking = booking;
//
        rooms.forEach(room -> {
            room.setStatus(RoomStatus.FULL);
            room.setBookingId(bookingId);
        });

        roomRepository.saveAll(rooms);

        return booking;
    }

//    @Override
//    public boolean areRoomsAvailableAndEmpty(List<Room> rooms, Date checkinDate, Date checkoutDate) {
//        List<Long> roomIds = rooms.stream().map(Room::getRoomid).collect(Collectors.toList());
//        List<Booking> overlappingBookings = bookingRepository.findOverlappingBookings(roomIds, checkinDate, checkoutDate);
//
//        return overlappingBookings.isEmpty() &&
//                rooms.stream().allMatch(room -> room.getStatus() == RoomStatus.EMPTY);
//    }

    public boolean areRoomsAvailableAndEmpty(List<Long> roomIds, Date checkinDate, Date checkoutDate) {
        return roomRepository.countAvailableRooms(roomIds, checkinDate, checkoutDate) == roomIds.size();
    }

    @Override
    public BigDecimal calculateTotalMoney(List<Room> rooms, Date checkinDate, Date checkoutDate) {
        long days = (checkoutDate.getTime() - checkinDate.getTime()) / (1000 * 60 * 60 * 24);
        System.out.println(days);
        BigDecimal totalMoney = rooms.stream()
                .map(room -> room.getPrice().multiply(BigDecimal.valueOf(days)))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println(totalMoney);
        return totalMoney;
    }

    @Override
    public void checkoutBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        List<Room> rooms = booking.getRooms();
        rooms.forEach(room -> {
            room.setStatus(RoomStatus.EMPTY);
            room.setBookingId(null);
        });
        roomRepository.saveAll(rooms);

        booking.setPaymentstatus("PAID");
        bookingRepository.save(booking);
    }

    @Override
    public BookingDetailResponse getBookingDetail(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        return BookingDetailResponse.builder()
                .bookingId(booking.getBookingId())
                .name(booking.getUser().getName())
                .userId(booking.getUser().getUserID())
                .roomIds(booking.getRooms().stream()
                        .map(Room::getRoomid)
                        .collect(Collectors.toList()))
                .prices(booking.getRooms().stream()
                        .map(Room::getPrice)
                        .collect(Collectors.toList()))
                .checkinDate(booking.getCheckindate())
                .checkoutDate(booking.getCheckoutdate())
                .bookingDate(booking.getBookingdate())
                .totalMoney(booking.getTotalmoney())
                .build();
    }

    @Autowired
    private PDFGeneratorService pdfGeneratorService;

    @Override
    public byte[] generateBookingInvoice(Long bookingId) throws IOException, DocumentException {
        BookingDetailResponse bookingDetail = getBookingDetail(bookingId);
        return pdfGeneratorService.generateBookingInvoice(bookingDetail);
    }

    private Booking getBookingById(long bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
    }
}
