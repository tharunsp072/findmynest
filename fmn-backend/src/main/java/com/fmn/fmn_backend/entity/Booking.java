package com.fmn.fmn_backend.entity;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fmn.fmn_backend.model.BookingStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @ManyToOne
    @JoinColumn(name = "tenant_id")
    @JsonIgnoreProperties({ "bookings", "user" })
    private TenantProfile tenant;

    @ManyToOne
    @JoinColumn(name = "property_id")
    @JsonIgnoreProperties({ "bookings", "owner" })
    private Property property;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    @JsonIgnore
    private OwnerProfile ownerProfile;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"booking"})
    private List<Payment> payments;


}
