package com.fmn.fmn_backend.service.PropertyService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fmn.fmn_backend.entity.Property;
import com.fmn.fmn_backend.repository.PropertyRepository;

@Service
public class PropertyServiceImpl implements PropertyService {

    @Autowired
    private PropertyRepository propertyRepo;

    @Override
    public List<Property> findAllProperties() {
        return propertyRepo.findAll();
    }

    @Override
    public List<Property> findByDescription(String description) {
        return propertyRepo.findAllByDescription(description);
    }

    @Override
    public List<Property> findByAddress(String address) {
        return propertyRepo.findAllByAddress(address);
    }

    @Override
    public List<Property> findByStatus(String status) {
        return propertyRepo.findAllByStatus(status);
    }

    @Override
    public List<Property> findByAgeOfBuilding(int age) {
        return propertyRepo.findAllByAgeOfBuilding(age);
    }

    @Override
    public Property findPropertyById(Long propertyId) {
        return propertyRepo.findById(propertyId)
                .orElse(null); // or throw exception if preferred
    }

}
