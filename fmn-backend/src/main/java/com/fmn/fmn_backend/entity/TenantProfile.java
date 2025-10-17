package com.fmn.fmn_backend.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
public class TenantProfile {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long tenantId;

  private String username;
  private String contact_number;
  private String preferences;

  @OneToOne
  @JoinColumn(name = "user_id")
  @ToString.Exclude
  private User user;

  @OneToMany(mappedBy = "tenant", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonIgnore
  private List<Booking> bookings;

  @ManyToOne
  @JoinColumn(name = "owner_id")
  @JsonIgnore
  private OwnerProfile owner;

  @Override
  public String toString() {
    return "TenantProfile [tenantId=" + tenantId
        + ", username=" + username
        + ", contact_number=" + contact_number
        + ", preferences=" + preferences
        + ", userId=" + (user != null ? user.getUserId() : null)
        + ", ownerId=" + (owner != null ? owner.getOwnerId() : null) + "]";
  }

}
