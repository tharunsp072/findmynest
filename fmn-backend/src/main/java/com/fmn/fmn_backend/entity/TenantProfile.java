package com.fmn.fmn_backend.entity;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
@Entity
@Data
public class TenantProfile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long tenantId;

    private String username;
    private String contact_number;
    private String preferences;

    @OneToOne
    @JoinColumn(name="user_id")
      @ToString.Exclude
    private User user;

    @OneToMany(mappedBy = "tenant", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"tenant", "property", "payments"})
    private List<Booking> bookings;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    @JsonIgnoreProperties({ "properties" })
    private OwnerProfile owner;
    


}

