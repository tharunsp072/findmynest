package com.fmn.fmn_backend.service.InquiryService;

import java.util.List;

import com.fmn.fmn_backend.entity.Inquiry;

public interface InquiryService {
        Inquiry saveInquiry(Long tenantId,Long propertyId,Inquiry inquiry);
        
        List<Inquiry> getInquiriesByOwner(Long ownerId);

        List<Inquiry> getInquiriesByTenant(Long tenantId);
} 