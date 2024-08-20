package com.code.hotel_management.model;

import com.code.hotel_management.util.RoomStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roomid")
    private Long roomid;

    @Column(name = "roomtype")
    private String roomType;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RoomStatus status;

    @Column(name = "description")
    private String description;

    @Column(name = "bookingid")
    private Long bookingId;

    @ManyToOne
    @JoinColumn(name = "bookingid", insertable = false, updatable = false)
    private Booking booking;
}