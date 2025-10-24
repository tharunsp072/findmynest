package com.fmn.fmn_backend.service.PaymentService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fmn.fmn_backend.entity.Booking;
import com.fmn.fmn_backend.entity.Payment;
import com.fmn.fmn_backend.model.PaymentStatus;

@Component
public class PaymentFactory {
    public Payment createPendingPayment(double amount, Booking booking, String mode, int monthNumber,
            LocalDate dueDate) {
        Payment payment = new Payment();
        payment.setPrice(amount);
        payment.setPaidAmount(0.0);
        payment.setPaymentMode(mode);
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setBooking(booking);
        payment.setMonthNumber(monthNumber);
        payment.setDueDate(dueDate);
        return payment;
    }

    public List<Payment> createMonthlyPayments(Booking booking, String mode, int totalMonths, double monthlyAmount) {
        List<Payment> payments = new ArrayList<>();
        LocalDate start = booking.getStartDate();

        for (int month = 1; month <= totalMonths; month++) {
            LocalDate dueDate = start.plusMonths(month - 1);
            Payment payment = createPendingPayment(monthlyAmount, booking, mode, month, dueDate);
            payments.add(payment);
        }

        return payments;
    }
}
