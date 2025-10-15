package com.fmn.fmn_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fmn.fmn_backend.entity.Inquiry;
import com.fmn.fmn_backend.service.InquiryService.InquiryService;

@CrossOrigin(origins = "http://localhost:3333")
@RestController
@RequestMapping("/inquiry")
public class InquiryController {
    @Autowired
    private InquiryService inquiryService;
    @PostMapping("/tenant/{tenantId}/property/{propertyId}")
    public ResponseEntity<?> saveInquiry(@PathVariable Long tenantId,
                                         @PathVariable Long propertyId,
                                         @RequestBody Inquiry inquiry) {
        System.out.println("Authenticated user: " + SecurityContextHolder.getContext().getAuthentication());
        System.out.println("TenantId path variable: " + tenantId);
        System.out.println("PropertyId path variable: " + propertyId);

        try {
            Inquiry saved = inquiryService.saveInquiry(tenantId, propertyId, inquiry);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/owner/{ownerId}")
    public List<Inquiry> getOwnerInquiries(@PathVariable Long ownerId) {
        return inquiryService.getInquiriesByOwner(ownerId);
    }

    @GetMapping("/tenant/{tenantId}")
    public List<Inquiry> getTenantInquiries(@PathVariable Long tenantId) {
        return inquiryService.getInquiriesByTenant(tenantId);
    }
}
