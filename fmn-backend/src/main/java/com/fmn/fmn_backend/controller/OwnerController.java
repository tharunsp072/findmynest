package com.fmn.fmn_backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.fmn.fmn_backend.dto.OwnerDTO.OwnerDTO;
import com.fmn.fmn_backend.entity.OwnerProfile;
import com.fmn.fmn_backend.entity.Property;
import com.fmn.fmn_backend.entity.TenantProfile;
import com.fmn.fmn_backend.service.OwnerService.OwnerService;

@RestController
@RequestMapping("/owners")
public class OwnerController {

    private OwnerService ownerService;

    @Autowired
    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @PostMapping("/saveOwner/{user_id}")
    public String saveOwner(@PathVariable Long user_id, @RequestBody OwnerProfile owner) {
        ownerService.saveOwner(user_id, owner);
        return "Owner saved";
    }

    @GetMapping("/findOwner/{id}")
    public OwnerDTO findOwner(@PathVariable Long id) {
        return ownerService.findById(id);
    }

    // @PostMapping("/listProperties/{userId}")
    // public Property listProperties(
    // @PathVariable Long userId,
    // @ModelAttribute PropertyDTO propertyDTO, // <-- binds all text/number fields
    // @RequestParam("imgUrl") MultipartFile imageFile) { // <-- file separately

    // Property property = new Property();
    // property.setTitle(propertyDTO.getTitle());
    // property.setDescription(propertyDTO.getDescription());
    // property.setPrice(propertyDTO.getPrice());
    // property.setAddress(propertyDTO.getAddress());
    // property.setStatus(propertyDTO.getStatus());
    // property.setCarpetArea(propertyDTO.getCarpetArea());
    // property.setAgeOfBuilding(propertyDTO.getAgeOfBuilding());
    // property.setFurnishedStatus(propertyDTO.getFurnishedStatus());

    // return ownerService.saveProperty(userId, property, imageFile);
    // }

    @PostMapping("/listProperties/{userId}")
    public Property listProperties(@PathVariable Long userId, @RequestBody Property property) {
        return ownerService.saveProperty(userId, property);
    }

    @GetMapping("/getProperties/{userId}")
    public List<Property> findAllPropertiesOfOwner(@PathVariable Long userId) {
        return ownerService.findAllpropertiesOfOwner(userId);
    }

    @GetMapping("/getTenants/{ownerId}")
    public List<TenantProfile> findAllTenantsOfOwner(@PathVariable Long ownerId) {
        return ownerService.findAllTenantsOfOwner(ownerId);
    }

    @DeleteMapping("/deleteProperty/property/{propertyId}/user/{userId}")
    public void deleteOwnerProperty(@PathVariable("propertyId") Long propertyId, @PathVariable("userId") Long userId) {
        ownerService.deleteOwnerProperty(propertyId, userId);
    }

    @PutMapping("/updateOwner/{userId}")
    public OwnerProfile updateOwnerDetails(@PathVariable("userId") Long userId,
            @RequestBody OwnerProfile ownerProfile) {
        return ownerService.updateOwnerDetails(userId, ownerProfile);
    }

    @GetMapping("/{ownerId}/stats")
    public Map<String, Object> getStats(@PathVariable Long ownerId) {
        return ownerService.getOwnerStats(ownerId);
    }
}
