package com.fmn.fmn_backend.service.PaymentService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fmn.fmn_backend.entity.Booking;
import com.fmn.fmn_backend.entity.Payment;
import com.fmn.fmn_backend.repository.BookingRepository;
import com.fmn.fmn_backend.repository.PaymentRepository;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepo;

    @Autowired
    private BookingRepository bookingRepo;
    
    @Override
    public Payment processPayment(Long bookingId, Payment payment) throws Exception {
      Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new Exception("Booking not found"));

        payment.setBooking(booking);
        payment.setPaymentStatus("SUCCESS");
        return paymentRepo.save(payment);
    }

    @Override
    public List<Payment> getPaymentsByTenant(Long tenantId) {
       return paymentRepo.findByBooking_Tenant_TenantId(tenantId);
    }

    @Override
    public List<Payment> getPaymentsByOwner(Long ownerId) {
        return paymentRepo.findByBooking_Property_Owner_OwnerId(ownerId);
    }
    
}
