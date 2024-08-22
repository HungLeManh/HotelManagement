package com.code.hotel_management.service.impl;

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
    @Override
    @Transactional
    public Booking createBooking(Long userId, List<Long> roomIds, Date checkinDate, Date checkoutDate) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Room> rooms = roomRepository.findAllById(roomIds);
        if (rooms.size() != roomIds.size()) {
            throw new ResourceNotFoundException("One or more rooms not found");
        }

        long countRoom = roomRepository.countAvailableRooms(roomIds, checkinDate, checkoutDate);

        System.out.println(countRoom);

        if (!areRoomsAvailableAndEmpty(roomIds, checkinDate, checkoutDate)) {
            throw new IllegalStateException("One or more rooms are not available or not empty for the selected dates");
        }

//        Room room1 = rooms.get(0);
//        Booking booking1 = getBookingById(room1.getBookingId());
//        if(checkoutDate.before(booking1.getCheckindate())){
//            throw new RuntimeException("please create checkoutDate after" + booking1.getCheckindate() );
//        }

        BigDecimal totalMoney = calculateTotalMoney(rooms, checkinDate, checkoutDate);

        Date currentDate= new Date();

        Booking booking = Booking.builder()
                .user(user)
                .rooms(rooms)
                .checkindate(checkinDate)
                .checkoutdate(checkoutDate)
                .bookingdate(currentDate)
                .totalmoney(totalMoney)
                .paymentstatus("PENDING")
                .build();

        booking = bookingRepository.save(booking);

        // Cập nhật bookingId cho các phòng
        Booking finalBooking = booking;


        if(checkinDate.equals(currentDate) || checkinDate.before(currentDate)){
            rooms.forEach(room -> {
                room.setStatus(RoomStatus.FULL);
                room.setBookingId(finalBooking.getBookingId());
            });
        }

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
