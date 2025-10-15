package com.fmn.fmn_backend.dto;

import lombok.Data;

@Data
public class PropertyDTO {
    private String title;
    private String description;
    private int price;
    private String address;
    private String status;
    private int carpetArea;
    private int ageOfBuilding;
    private String furnishedStatus;
}
