package com.fmn.fmn_backend.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fmn.fmn_backend.model.PaymentStatus;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    private LocalDate paymentDate = LocalDate.now(); // when payment is created or made
    private LocalDate dueDate; // when payment is due (for monthly payments)
    private Double price;
    private Double paidAmount; // amount for the current payment
    private String paymentMode; // UPI, CARD, BANK_TRANSFER

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus; // PENDING, SUCCESS, FAILED
    private int monthNumber; // which month this payment belongs to (1 = first month)

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bookingId")
    @JsonIgnore
    private Booking booking;

    @Override
    public String toString() {
        return "Payment [paymentId=" + paymentId + ", paymentDate=" + paymentDate + ", dueDate=" + dueDate + ", price="
                + price + ", paidAmount=" + paidAmount + ", paymentMode=" + paymentMode + ", paymentStatus="
                + paymentStatus + ", monthNumber=" + monthNumber + "]";
    }

   

   
}
