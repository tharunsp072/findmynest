package com.fmn.fmn_backend.service.PropertyService;

import java.util.List;

import com.fmn.fmn_backend.dto.PropertyDTO.PropertyDTO;
import com.fmn.fmn_backend.entity.Property;

public interface PropertyService {
        List<Property> findAllProperties();
        List<Property> findByDescription(String description );
        List<Property> findByAddress(String address);
        // List<Property> findByStatus(String status);
        List<Property> findByAgeOfBuilding(int age);
        List<PropertyDTO> findAllNotConfirmedProperties();
        Property findPropertyById(Long propertyId);
} 