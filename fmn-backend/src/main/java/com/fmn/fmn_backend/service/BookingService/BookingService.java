package com.fmn.fmn_backend.service.BookingService;

import java.util.List;
import com.fmn.fmn_backend.entity.Booking;

public interface BookingService {
    List<Booking> findAllBookings();

    Booking saveBooking(Long tenantId, Long propertyId, Booking booking) throws Exception;
}
