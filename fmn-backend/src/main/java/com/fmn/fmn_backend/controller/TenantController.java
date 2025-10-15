package com.fmn.fmn_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.fmn.fmn_backend.entity.Booking;
import com.fmn.fmn_backend.entity.TenantProfile;
import com.fmn.fmn_backend.service.TenantService.TenantService;

@RestController
@RequestMapping("/tenants")
public class TenantController {

    @Autowired
    private TenantService tenantService;
    
    @PostMapping("/saveTenant/{user_id}")
    public String saveTenant(@PathVariable Long user_id,@RequestBody TenantProfile tenant){
        tenantService.saveTenant(user_id, tenant);
        return "Tenant Saved";
    }

    @GetMapping("/findTenant/{userId}")
    public TenantProfile findTenant(@PathVariable Long userId){
        return tenantService.findTenant(userId);
    }

    @PostMapping("/saveBooking/{userId}/propertyId={propertyId}")
    public Booking saveBooking(@PathVariable("userId") Long userId,
            @PathVariable("propertyId") Long propertyId,
            @RequestBody Booking booking) {
        return tenantService.saveBooking(userId, propertyId, booking);
    }
    
    @GetMapping("/findAllBookings/{tenant_id}")
    public List<Booking> findAllBookings(@PathVariable Long  tenant_id) {
        return tenantService.getAllBookingsByTenant(tenant_id);
    }

    @PutMapping("/updateTenant/{userId}")
    public TenantProfile updateTenantDetails(@PathVariable Long userId,@RequestBody TenantProfile tenant){
        return tenantService.updateTenantDetails(userId,tenant);
    }
}
