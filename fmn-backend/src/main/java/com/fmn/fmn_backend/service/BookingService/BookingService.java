package com.fmn.fmn_backend.service.BookingService;

import java.util.List;

import com.fmn.fmn_backend.dto.BookingDTO.BookingDTO;
import com.fmn.fmn_backend.dto.BookingDTO.BookingResponseDTO;
import com.fmn.fmn_backend.entity.Booking;


public interface BookingService {
    List<BookingResponseDTO> findAllBookings();
    List<BookingResponseDTO> findBookingsByOwnerId(Long ownerId);
    BookingResponseDTO saveBooking(Long tenantId, Long propertyId, BookingDTO booking);
    Booking updateBookingStatus(Long ownerId, Long bookingId,String status);
    void deleteExistingBooking(Long ownerId, Long bookingId);
    List<BookingResponseDTO> findBookingsByTenantId(Long tenantId);
    List<BookingResponseDTO> findConfirmedBookingsByTenantId(long tenantId);
}
