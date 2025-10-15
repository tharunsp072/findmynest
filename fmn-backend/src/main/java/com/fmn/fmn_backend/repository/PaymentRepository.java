package com.fmn.fmn_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fmn.fmn_backend.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment,Long>{
    List<Payment> findByBooking_Tenant_TenantId(Long tenantId);

    List<Payment> findByBooking_Property_Owner_OwnerId(Long ownerId);
}
