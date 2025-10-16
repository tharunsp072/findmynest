package com.fmn.fmn_backend.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
public class OwnerProfile {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ownerId;

    private String fullname;
    private String contact_number;
    private String address;
    private String total_revenue;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "owner",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"owner", "bookings"})
    private List<Property> properties;

    @OneToMany(mappedBy = "owner",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<TenantProfile> tenants;

    @Override
    public String toString() {
        return "OwnerProfile [ownerId=" + ownerId + ", fullname=" + fullname + ", contact_number=" + contact_number
                + ", address=" + address + ", total_revenue=" + total_revenue + "]";
    }

    
}
