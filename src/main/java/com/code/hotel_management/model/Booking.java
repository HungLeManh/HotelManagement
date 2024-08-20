package com.code.hotel_management.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookingid")
    private Long bookingId;

    @ManyToOne
    @JoinColumn(name = "userid")
    private User user;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private List<Room> rooms;

    @Temporal(TemporalType.DATE)
    @Column(name = "checkindate")
    private Date checkindate;

    @Temporal(TemporalType.DATE)
    @Column(name = "checkoutdate")
    private Date checkoutdate;

    @Temporal(TemporalType.DATE)
    @Column(name = "bookingdate")
    private Date bookingdate;

    @Column(name = "totalmoney")
    private BigDecimal totalmoney;

    @Column(name = "paymentstatus")
    private String paymentstatus;
}