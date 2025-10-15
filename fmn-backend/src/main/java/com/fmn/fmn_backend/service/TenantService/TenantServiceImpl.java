package com.fmn.fmn_backend.service.TenantService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;

import com.fmn.fmn_backend.entity.Booking;
import com.fmn.fmn_backend.entity.Property;
import com.fmn.fmn_backend.entity.TenantProfile;
import com.fmn.fmn_backend.entity.User;
import com.fmn.fmn_backend.repository.BookingRepository;
import com.fmn.fmn_backend.repository.PropertyRepository;
import com.fmn.fmn_backend.repository.TenantRepository;
import com.fmn.fmn_backend.repository.UserRepository;

@Service
public class TenantServiceImpl implements TenantService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TenantRepository tenantRepo;

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private PropertyRepository propertyRepository;
    @Override
    public TenantProfile saveTenant(Long user_id, TenantProfile tenant) {
        User user = userRepository.findById(user_id).orElseThrow(() -> new RuntimeException("User not found"));
        tenant.setUser(user);
        return tenantRepo.save(tenant);
    }

    @Override
    public List<Booking> getAllBookingsByTenant(Long tenantId) {
        return bookingRepo.findByTenantTenantId(tenantId);
    }

    @Override

    public Booking saveBooking(Long userId,Long propertyId, Booking booking) {
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("User not found"));
        TenantProfile tenant = tenantRepo.findByUser(user);
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));
        booking.setProperty(property);
        booking.setTenant(tenant);
        return bookingRepo.save(booking);
    }

    @Override
    public TenantProfile findTenant(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        TenantProfile tenant = tenantRepo.findByUser(user);
        return tenant;
    }

    @Override
    public TenantProfile updateTenantDetails(Long userId,TenantProfile tenant) {
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User not found"));
        TenantProfile oldTenant = tenantRepo.findByUser(user);
        oldTenant.setUsername(tenant.getUsername());
        oldTenant.setContact_number(tenant.getContact_number());
        oldTenant.setPreferences(tenant.getPreferences());
        return tenantRepo.save(oldTenant);
    }
}
