package com.fmn.fmn_backend.dto;

import java.time.LocalDate;
import java.util.List;

import com.fmn.fmn_backend.entity.Payment;

import lombok.Data;

@Data
public class PaymentDTO {
    private Long id;
    private String tenantName;
    private String propertyTitle;
    private Double price;
    private Double paidAmount;
    private LocalDate paymentDate;

    public PaymentDTO(Payment payment) {
        this.id = payment.getPaymentId();
        this.tenantName = payment.getBooking().getTenant().getUsername();
        this.propertyTitle = payment.getBooking().getProperty().getTitle();
        this.price = (double) payment.getBooking().getProperty().getPrice();
        this.paidAmount = payment.getPaidAmount();
        this.paymentDate = payment.getPaymentDate();
    }

    public PaymentDTO(List<PaymentDTO> payments) {
        // TODO Auto-generated constructor stub
    }

}
