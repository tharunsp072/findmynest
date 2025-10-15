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

import com.fmn.fmn_backend.entity.Booking;
import com.fmn.fmn_backend.service.BookingService.BookingService;
import com.fmn.fmn_backend.service.PropertyService.PropertyService;
import com.fmn.fmn_backend.service.TenantService.TenantService;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final PropertyService propertyService; 
    private final TenantService tenantService; 

    @Autowired
    public BookingController(BookingService bookingService, PropertyService propertyService,
            TenantService tenantService) {
        this.bookingService = bookingService;
        this.propertyService = propertyService;
        this.tenantService = tenantService;
    }

    @GetMapping("/findAll")
    public List<Booking> findAllBookings() {
        return bookingService.findAllBookings();
    }

   @PostMapping("/saveBooking/{tenantId}/property/{propertyId}")
public ResponseEntity<?> saveBooking(
        @PathVariable Long tenantId,
        @PathVariable Long propertyId,
        @RequestBody Booking booking) {
    try {
        Booking savedBooking = bookingService.saveBooking(tenantId, propertyId, booking);
        return ResponseEntity.ok(savedBooking);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
}

}
