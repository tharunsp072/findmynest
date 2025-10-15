package com.fmn.fmn_backend.service.TenantService;

import java.util.List;

import com.fmn.fmn_backend.entity.Booking;
import com.fmn.fmn_backend.entity.TenantProfile;

public interface TenantService {
    TenantProfile saveTenant(Long user_id,TenantProfile tenant);
    List<Booking> getAllBookingsByTenant(Long tenantId);
    Booking saveBooking(Long userId,Long propertyId,Booking booking);
    TenantProfile findTenant(Long userId);
    TenantProfile updateTenantDetails(Long userId,TenantProfile tenant);
} 
