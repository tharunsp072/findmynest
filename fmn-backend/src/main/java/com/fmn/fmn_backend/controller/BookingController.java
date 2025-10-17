package com.fmn.fmn_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fmn.fmn_backend.dto.BookingDTO.BookingDTO;
import com.fmn.fmn_backend.dto.BookingDTO.BookingResponseDTO;
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
    public List<BookingResponseDTO> findAllBookings() {
        System.out.println(bookingService.findAllBookings());
        return bookingService.findAllBookings();
    }

   @PostMapping("/saveBooking/{tenantId}/property/{propertyId}")
public ResponseEntity<?> saveBooking(
        @PathVariable Long tenantId,
        @PathVariable Long propertyId,
        @RequestBody BookingDTO bookingDTO) {
    try {
        BookingResponseDTO savedBooking = bookingService.saveBooking(tenantId, propertyId, bookingDTO);
        return ResponseEntity.ok(savedBooking);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
}

@GetMapping("/owners/findBookings/{ownerId}")
public List<BookingResponseDTO> findBookingByOwnerId(@PathVariable Long ownerId){
    return bookingService.findBookingsByOwnerId(ownerId);
}

@PutMapping("/owners/{ownerId}/updateBooking/{bookingId}")
public Booking updateBookingStatus(@PathVariable Long ownerId,@PathVariable Long bookingId,@RequestBody String status){
    return bookingService.updateBookingStatus(ownerId,bookingId,status);
}

@DeleteMapping("/owners/{ownerId}/deleteBooking/{bookingId}")
public void deleteExistingBooking(@PathVariable Long ownerId,@PathVariable Long bookingId){
    bookingService.deleteExistingBooking(ownerId,bookingId);
}

@GetMapping("/tenants/{tenantId}/findBookings")
public List<BookingResponseDTO> findBookingByTenantId(@PathVariable Long tenantId){
    return bookingService.findBookingsByTenantId(tenantId);
}
}
