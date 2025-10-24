package com.fmn.fmn_backend.dto.PropertyDTO;

import com.fmn.fmn_backend.entity.Property;
import com.fmn.fmn_backend.model.AvailableStatus;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
public class PropertyDTO {
    public PropertyDTO(Property property) {
        if (property != null) {
            this.propertyId=property.getPropertyId();
            this.title = property.getTitle();
            this.description = property.getDescription();
            this.price = property.getPrice();
            this.address = property.getAddress();
            this.status = property.getStatus();
            this.carpetArea = property.getCarpetArea();
            this.ageOfBuilding = property.getAgeOfBuilding();
            this.furnishedStatus = property.getFurnishedStatus();
            this.imgUrl = property.getImgUrl();
        }
    }

    private Long propertyId;
        private String title;
    private String description;
    private double price;
    private String address;
    private AvailableStatus status;
    private int carpetArea;
    private int ageOfBuilding;
    private String furnishedStatus;
    private String imgUrl;
}
