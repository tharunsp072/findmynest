package com.fmn.fmn_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fmn.fmn_backend.entity.Payment;
import com.fmn.fmn_backend.service.PaymentService.PaymentService;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    
    @Autowired
    private PaymentService paymentService;

    
    @PostMapping("/process/{bookingId}")
    public ResponseEntity<?> processPayment(@PathVariable Long bookingId,
                                            @RequestBody Payment payment) {
        try {
            Payment saved = paymentService.processPayment(bookingId, payment);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/tenant/{tenantId}")
    public List<Payment> getTenantPayments(@PathVariable Long tenantId) {
        return paymentService.getPaymentsByTenant(tenantId);
    }

    @GetMapping("/owner/{ownerId}")
    public List<Payment> getOwnerPayments(@PathVariable Long ownerId) {
        return paymentService.getPaymentsByOwner(ownerId);
    }
}
