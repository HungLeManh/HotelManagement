package com.code.hotel_management.service;

import jakarta.mail.MessagingException;

public interface EmailService {
    void sendBookingConfirmationEmail(String to, String subject, String content) throws MessagingException;
}
