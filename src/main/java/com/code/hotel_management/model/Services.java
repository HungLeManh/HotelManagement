package com.code.hotel_management.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "services")
public class Services {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "serviceid")
    private Long serviceId;

    @Column(name = "servicename")
    private String serviceName;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private BigDecimal price;

    @OneToMany(mappedBy = "service")
    @JsonBackReference
    private List<ServiceBooking> serviceBookings;
}
