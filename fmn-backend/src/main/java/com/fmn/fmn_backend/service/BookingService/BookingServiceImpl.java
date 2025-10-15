package com.fmn.fmn_backend.service.BookingService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fmn.fmn_backend.entity.Booking;
import com.fmn.fmn_backend.entity.OwnerProfile;
import com.fmn.fmn_backend.entity.Property;
import com.fmn.fmn_backend.entity.TenantProfile;
import com.fmn.fmn_backend.model.BookingStatus;
import com.fmn.fmn_backend.repository.BookingRepository;
import com.fmn.fmn_backend.repository.PropertyRepository;
import com.fmn.fmn_backend.repository.TenantRepository;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private PropertyRepository propertyRepo;

    @Autowired
    private TenantRepository tenantRepo;

    @Override
    public List<Booking> findAllBookings() {
        return bookingRepo.findAll();
    }

    @Override
    public Booking saveBooking(Long tenantId, Long propertyId, Booking booking) throws Exception {

        TenantProfile tenant = tenantRepo.findById(tenantId)
                .orElseThrow(() -> new Exception("Tenant not found"));

        Property property = propertyRepo.findById(propertyId)
                .orElseThrow(() -> new Exception("Property not found"));


        boolean isBooked = bookingRepo.existsByProperty_PropertyIdAndStatus(
                property.getPropertyId(),
                BookingStatus.CONFIRMED);

        if (isBooked) {
            throw new Exception("Property is already booked.");
        }

        OwnerProfile owner = property.getOwner();
        booking.setTenant(tenant);
        booking.setProperty(property);
        booking.setOwnerProfile(owner);
        booking.setStatus(BookingStatus.PENDING);

 
        return bookingRepo.save(booking);
    }
}
