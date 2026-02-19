package com.example.siapraja.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "presences")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Presence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "monitoring_id")
    private Monitoring monitoring;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String location;

    private String notes;

    @Column(nullable = false)
    private int status;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "checkin_time", nullable = false)
    private LocalTime checkinTime;

    @Column(name = "checkout_time")
    private LocalTime checkoutTime;
}
