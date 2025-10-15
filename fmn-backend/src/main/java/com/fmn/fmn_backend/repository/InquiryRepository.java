package com.fmn.fmn_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fmn.fmn_backend.entity.Inquiry;

public interface InquiryRepository extends JpaRepository<Inquiry,Long> {
    List<Inquiry> findByProperty_Owner_OwnerId(Long ownerId);

    List<Inquiry> findByTenant_TenantId(Long tenantId);
}
