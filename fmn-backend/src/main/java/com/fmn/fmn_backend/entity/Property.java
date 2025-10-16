    package com.fmn.fmn_backend.entity;

    import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

    import jakarta.persistence.*;
    import lombok.Data;

    @Entity
    @Data
    public class Property {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name="PROPERTY_ID")
        private Long propertyId;
        private String title;
        private String description;
        private int price;
        private String address;
        private String status;
        private int carpetArea;
        private int ageOfBuilding;
        private String furnishedStatus;
        private String imgUrl;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "ownerId")
        @JsonIgnore
        private OwnerProfile owner;
        
        @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
        @JsonBackReference
        private List<Booking> bookings;

        @Override
        public String toString() {
            return "Property [propertyId=" + propertyId + ", title=" + title + ", description=" + description
                    + ", price=" + price + ", address=" + address + ", status=" + status + ", carpetArea=" + carpetArea
                    + ", ageOfBuilding=" + ageOfBuilding + ", furnishedStatus=" + furnishedStatus + ", imgUrl=" + imgUrl
                    + "]";
        }

        
    }
