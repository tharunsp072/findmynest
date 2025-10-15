package com.fmn.fmn_backend.service.PaymentService;

import java.util.List;

import com.fmn.fmn_backend.entity.Payment;

public interface PaymentService {
 Payment processPayment(Long bookingId, Payment payment) throws Exception;
 List<Payment> getPaymentsByTenant(Long tenantId);
 List<Payment> getPaymentsByOwner(Long ownerId);    
}