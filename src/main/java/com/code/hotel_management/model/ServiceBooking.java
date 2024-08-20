package com.code.hotel_management.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "servicebookings")
public class ServiceBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "servicebookingid")
    private Long serviceBookingId;

    @ManyToOne
    @JoinColumn(name = "userid")
    private User user;

    @ManyToOne
    @JoinColumn(name = "serviceid")
    @JsonManagedReference
    private Services service;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "totalprice")
    private BigDecimal totalPrice;

    @Column(name = "paymentstatus")
    private String paymentStatus;
}
