package com.fmn.fmn_backend.service.InquiryService;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fmn.fmn_backend.entity.Inquiry;
import com.fmn.fmn_backend.entity.Property;
import com.fmn.fmn_backend.entity.TenantProfile;
import com.fmn.fmn_backend.entity.User;
import com.fmn.fmn_backend.repository.InquiryRepository;
import com.fmn.fmn_backend.repository.PropertyRepository;
import com.fmn.fmn_backend.repository.TenantRepository;
import com.fmn.fmn_backend.repository.UserRepository;

@Service
public class InquiryServiceImpl implements InquiryService{

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private TenantRepository tenantRepo;

    @Autowired
    private PropertyRepository propertyRepo;

    @Autowired
    private InquiryRepository inquiryRepo;
    @Override
    public Inquiry saveInquiry(Long tenantId, Long propertyId, Inquiry inquiry) {

        TenantProfile tenant = tenantRepo.findById(tenantId)
            .orElseThrow(() -> new RuntimeException("Tenant not found"));

        Property property = propertyRepo.findById(propertyId)
            .orElseThrow(() -> new RuntimeException("Property not found"));

        inquiry.setTenant(tenant);
        inquiry.setProperty(property);

        
        inquiry.setStatus("PENDING");
        inquiry.setInquiryDate(LocalDate.now());

        return inquiryRepo.save(inquiry);
    }

    @Override
    public List<Inquiry> getInquiriesByOwner(Long ownerId) {
        return inquiryRepo.findByProperty_Owner_OwnerId(ownerId);
    }

    @Override
    public List<Inquiry> getInquiriesByTenant(Long tenantId) {
        return inquiryRepo.findByTenant_TenantId(tenantId);
    }
    
}
