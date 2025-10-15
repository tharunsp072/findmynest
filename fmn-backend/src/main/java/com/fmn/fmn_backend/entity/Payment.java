package com.fmn.fmn_backend.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    private LocalDate paymentDate = LocalDate.now();
    private Double amount;
    private String paymentMode;
    private String paymentStatus;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="bookingId")
    @JsonIgnoreProperties({"payments", "property", "tenant"})
    private Booking booking;
}
