package com.fmn.fmn_backend.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.*;

import com.fmn.fmn_backend.model.BookingStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tenant_id")
    // @JsonIgnore
    private TenantProfile tenant;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "property_id")
    // @JsonIgnore
    private Property property;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    @JsonIgnore
    private OwnerProfile ownerProfile;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Payment> payments;


    @Override
    public String toString() {
        return "Booking [bookingId=" + bookingId + ", status=" + status + "]";
    }

}
