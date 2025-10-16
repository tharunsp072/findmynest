package com.fmn.fmn_backend.entity;


import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Inquiry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inquiryId;

    private String message;
    private LocalDate inquiryDate = LocalDate.now();
    private String status = "PENDING";

    @ManyToOne
    @JoinColumn(name="propertyId")
    private Property property;

    @ManyToOne
    @JoinColumn(name="tenantId")
    private TenantProfile tenant;

    @Override
    public String toString() {
        return "Inquiry [inquiryId=" + inquiryId + ", message=" + message + ", inquiryDate=" + inquiryDate + ", status="
                + status + "]";
    }

    
}   
