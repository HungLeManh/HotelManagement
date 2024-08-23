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
@Table(name = "promotions")
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promotionid")
    private Long promotionId;

    @Column(name = "promotionname")
    private String promotionName;

    @Column(name = "description")
    private String description;

    @Column(name = "discountrate")
    private BigDecimal discountRate;

    @Column(name = "startdate")
    private Date startDate;

    @Column(name = "enddate")
    private Date endDate;

    @OneToMany(mappedBy = "promotion")
    private List<Booking> bookings;
}
