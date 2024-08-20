package com.code.hotel_management.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "paymentid")
    private Long paymentId;

    @ManyToOne
    @JoinColumn(name = "bookingid")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "servicebookingid")
    private ServiceBooking serviceBooking;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "paymentdate")
    private Date paymentDate;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "paymentmethod")
    private String paymentMethod;
}
