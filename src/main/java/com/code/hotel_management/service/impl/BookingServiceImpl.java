package com.code.hotel_management.service.impl;

import com.code.hotel_management.dto.request.BookingRequestDTO;
import com.code.hotel_management.dto.response.BookingDetailResponse;
import com.code.hotel_management.exception.ResourceNotFoundException;
import com.code.hotel_management.model.Booking;
import com.code.hotel_management.model.Promotion;
import com.code.hotel_management.model.Room;
import com.code.hotel_management.model.User;
import com.code.hotel_management.repository.BookingRepository;
import com.code.hotel_management.repository.PromotionRepository;
import com.code.hotel_management.repository.RoomRepository;
import com.code.hotel_management.repository.UserRepository;
import com.code.hotel_management.service.BookingService;
import com.code.hotel_management.service.EmailService;
import com.code.hotel_management.service.PDFGeneratorService;
import com.code.hotel_management.util.RoomStatus;
import com.itextpdf.text.DocumentException;
import jakarta.mail.MessagingException;
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
import java.text.SimpleDateFormat;
import java.util.Arrays;
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
    private final PromotionRepository promotionRepository;
    private final EntityManager entityManager;
    private final EmailService emailService;
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

        Booking booking = Booking.builder()
                .user(user)
                .rooms(rooms)
                .checkindate(checkinDate)
                .checkoutdate(checkoutDate)
                .bookingdate(new Date())
                .totalmoney(totalMoney)
                .paymentstatus("PENDING")
                .build();

        List<Promotion> validPromotions = promotionRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(booking.getBookingdate(), booking.getBookingdate());
        if (!validPromotions.isEmpty()) {
            Promotion applicablePromotion = validPromotions.get(0); // Assuming we apply the first valid promotion
            booking.setPromotion(applicablePromotion);

            BigDecimal newTotalMoney = totalMoney.subtract(totalMoney.multiply(applicablePromotion.getDiscountRate()));

            booking.setTotalmoney(newTotalMoney);
        }

        bookingRepository.save(booking);

        // Cập nhật bookingId cho các phòng
        rooms.forEach(room -> {
            room.setStatus(RoomStatus.FULL);
            room.setBookingId(booking.getBookingId());
        });

        roomRepository.saveAll(rooms);

        Booking savedBooking = bookingRepository.save(booking);

        // Send confirmation email
        try {
            sendBookingConfirmationEmail(savedBooking);
        } catch (MessagingException e) {
            log.error("Failed to send booking confirmation email", e);
        }

        return booking;
    }


    private void sendBookingConfirmationEmail(Booking booking) throws MessagingException {
        String to = booking.getUser().getEmail(); // Assuming User has an email field
        String subject = "Booking Confirmation - Booking ID: " + booking.getBookingId();
        String content = buildEmailContent(booking);

        emailService.sendBookingConfirmationEmail(to, subject, content);
    }

    private String buildEmailContent(Booking booking) {
        StringBuilder content = new StringBuilder();
        content.append("<h1>Booking Confirmation</h1>");
        content.append("<p>Dear ").append(booking.getUser().getName()).append(",</p>");
        content.append("<p>Your booking has been confirmed. Details are as follows:</p>");
        content.append("<ul>");
        content.append("<li>Booking ID: ").append(booking.getBookingId()).append("</li>");
        content.append("<li>Check-in Date: ").append(booking.getCheckindate()).append("</li>");
        content.append("<li>Check-out Date: ").append(booking.getCheckoutdate()).append("</li>");
        content.append("<li>Promotion: ").append(booking.getPromotion()).append("</li>");
        content.append("<li>Total Amount: $").append(booking.getTotalmoney()).append("</li>");
        content.append("</ul>");
        content.append("<h2>Booked Rooms:</h2>");
        content.append("<ul>");
        for (Room room : booking.getRooms()) {
            content.append("<li>Room ").append(room.getRoomid()).append(" - $").append(room.getPrice()).append(" per night</li>");
        }
        content.append("</ul>");
        content.append("<p>Thank you for choosing our hotel!</p>");
        return content.toString();
    }

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

//    @Override
//    public BookingDetailResponse getBookingDetail(Long bookingId) {
//        Booking booking = bookingRepository.findById(bookingId)
//                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
//
//        return BookingDetailResponse.builder()
//                .bookingId(booking.getBookingId())
//                .name(booking.getUser().getName())
//                .userId(booking.getUser().getUserID())
//                .roomIds(booking.getRooms().stream()
//                        .map(Room::getRoomid)
//                        .collect(Collectors.toList()))
//                .prices(booking.getRooms().stream()
//                        .map(Room::getPrice)
//                        .collect(Collectors.toList()))
//                .checkinDate(booking.getCheckindate())
//                .checkoutDate(booking.getCheckoutdate())
//                .bookingDate(booking.getBookingdate())
//                .totalMoney(booking.getTotalmoney())
//                .build();
//    }

    @Override
    public BookingDetailResponse getBookingDetail(Long bookingId) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("get_booking_detail")
                .registerStoredProcedureParameter("p_booking_id", Long.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_result_booking_id", Long.class, ParameterMode.OUT)
                .registerStoredProcedureParameter("p_name", String.class, ParameterMode.OUT)
                .registerStoredProcedureParameter("p_user_id", Long.class, ParameterMode.OUT)
                .registerStoredProcedureParameter("p_room_ids", Long[].class, ParameterMode.OUT)
                .registerStoredProcedureParameter("p_prices", BigDecimal[].class, ParameterMode.OUT)
                .registerStoredProcedureParameter("p_checkin_date", java.sql.Date.class, ParameterMode.OUT)
                .registerStoredProcedureParameter("p_checkout_date", java.sql.Date.class, ParameterMode.OUT)
                .registerStoredProcedureParameter("p_booking_date", java.sql.Date.class, ParameterMode.OUT)
                .registerStoredProcedureParameter("p_total_money", BigDecimal.class, ParameterMode.OUT)
                .setParameter("p_booking_id", bookingId);

        try {
            query.execute();

            Long resultBookingId = (Long) query.getOutputParameterValue("p_result_booking_id");
            if (resultBookingId == null) {
                throw new ResourceNotFoundException("Booking not found");
            }

            String name = (String) query.getOutputParameterValue("p_name");
            Long userId = (Long) query.getOutputParameterValue("p_user_id");
            Long[] roomIds = (Long[]) query.getOutputParameterValue("p_room_ids");
            BigDecimal[] prices = (BigDecimal[]) query.getOutputParameterValue("p_prices");
            java.sql.Date checkinDate = (java.sql.Date) query.getOutputParameterValue("p_checkin_date");
            java.sql.Date checkoutDate = (java.sql.Date) query.getOutputParameterValue("p_checkout_date");
            java.sql.Date bookingDate = (java.sql.Date) query.getOutputParameterValue("p_booking_date");
            BigDecimal totalMoney = (BigDecimal) query.getOutputParameterValue("p_total_money");

            List<Long> roomIdList = Arrays.asList(roomIds);
            List<BigDecimal> priceList = Arrays.asList(prices);

            return BookingDetailResponse.builder()
                    .bookingId(resultBookingId)
                    .name(name)
                    .userId(userId)
                    .roomIds(roomIdList)
                    .prices(priceList)
                    .checkinDate(checkinDate)
                    .checkoutDate(checkoutDate)
                    .bookingDate(bookingDate)
                    .totalMoney(totalMoney)
                    .build();
        } catch (Exception e) {
            throw new ResourceNotFoundException("Error retrieving booking details: " + e.getMessage());
        }
    }

    @Autowired
    private PDFGeneratorService pdfGeneratorService;

    @Override
    public byte[] generateBookingInvoice(Long bookingId) throws IOException, DocumentException {
        BookingDetailResponse bookingDetail = getBookingDetail(bookingId);
        return pdfGeneratorService.generateBookingInvoice(bookingDetail);
    }

    @Override
    public void cancelBooking(Long bookingId) {
        Booking booking = getBookingById(bookingId);
        List<Room> rooms = roomRepository.findAllById(booking.getRooms().stream()
                        .map(Room::getRoomid)
                        .collect(Collectors.toList()));

        Date nowDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        System.out.println(sdf.format(nowDate));

        if(sdf.format(booking.getBookingdate()).equals(sdf.format(nowDate))){
            System.out.println(nowDate);
            rooms.forEach(room -> {
                room.setStatus(RoomStatus.EMPTY);
                room.setBookingId(null);
            });

            roomRepository.saveAll(rooms);
            bookingRepository.delete(booking);

        }

    }

    private Booking getBookingById(long bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
    }
}
