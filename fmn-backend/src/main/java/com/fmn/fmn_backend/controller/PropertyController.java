package com.fmn.fmn_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.fmn.fmn_backend.dto.PropertyDTO.PropertyDTO;
import com.fmn.fmn_backend.entity.Property;
import com.fmn.fmn_backend.service.PropertyService.PropertyService;

@RestController
@RequestMapping("/properties")
public class PropertyController {
    
    private PropertyService propertyService;

    @Autowired 
    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping("/findAll")
    public List<Property> findAllProperties(){
        return propertyService.findAllProperties();
    }

    @GetMapping("/findNotConfirmed")
    public List<PropertyDTO> findPropertiesNotConfirmed(){
        return propertyService.findAllNotConfirmedProperties();
    }

    @GetMapping("/find/{age}")
    public List<Property> findPropertiesByAge(@PathVariable int age){
        return propertyService.findByAgeOfBuilding(age);
    }
    

    // @GetMapping("/find/{status}")
    // public List<Property> findPropertiesByStatus(@PathVariable String status){
    //     return propertyService.findByStatus(status);
    // }

     @GetMapping("/find/{description}")
    public List<Property> findPropertiesByDescription(@PathVariable String description){
        return propertyService.findByDescription(description);
    }

    @GetMapping("/find/{address}")
    public List<Property> findPropertiesByaddress(@PathVariable String address){
        return propertyService.findByAddress(address);
    }


    
}
