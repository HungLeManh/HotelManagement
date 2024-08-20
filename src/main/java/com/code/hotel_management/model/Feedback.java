package com.code.hotel_management.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "feedbacks")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedbackid")
    private Long feedbackid;

    @ManyToOne
    @JoinColumn(name = "userid")
    @JsonManagedReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "servicebookingid")
    @JsonManagedReference
    private ServiceBooking serviceBooking;

    @ManyToOne
    @JoinColumn(name = "bookingid")
    @JsonManagedReference
    private Booking booking;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "comments")
    private String comments;
}
