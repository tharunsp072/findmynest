package com.fmn.fmn_backend.service.BookingService;

import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fmn.fmn_backend.dto.PaymentDTO;
import com.fmn.fmn_backend.dto.BookingDTO.BookingDTO;
import com.fmn.fmn_backend.dto.BookingDTO.BookingResponseDTO;
import com.fmn.fmn_backend.dto.PropertyDTO.PropertyDTO;
import com.fmn.fmn_backend.dto.TenantDTO.TenantDTO;
import com.fmn.fmn_backend.entity.Booking;
import com.fmn.fmn_backend.entity.OwnerProfile;
import com.fmn.fmn_backend.entity.Payment;
import com.fmn.fmn_backend.entity.Property;
import com.fmn.fmn_backend.entity.TenantProfile;
import com.fmn.fmn_backend.model.AvailableStatus;
import com.fmn.fmn_backend.model.BookingStatus;
import com.fmn.fmn_backend.repository.BookingRepository;
import com.fmn.fmn_backend.repository.OwnerRepository;
import com.fmn.fmn_backend.repository.PropertyRepository;
import com.fmn.fmn_backend.repository.TenantRepository;
import com.fmn.fmn_backend.service.PaymentService.PaymentFactory;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private PropertyRepository propertyRepo;

    @Autowired
    private TenantRepository tenantRepo;

    @Autowired
    private PaymentFactory paymentFactory;

    @Autowired
    private OwnerRepository ownerRepo;

    @Override
    public List<BookingResponseDTO> findAllBookings() {
        return bookingRepo.findAll().stream()
                .map(booking -> {
                    BookingResponseDTO dto = new BookingResponseDTO();
                    dto.setBookingId(booking.getBookingId());
                    dto.setStartDate(booking.getStartDate());
                    dto.setEndDate(booking.getEndDate());
                    dto.setStatus(booking.getStatus().name());
                    dto.setProperty(new PropertyDTO(booking.getProperty()));
                    dto.setTenant(new TenantDTO(booking.getTenant()));
                    dto.setPayments(booking.getPayments().stream()
                            .map(payment -> new PaymentDTO(payment))
                            .toList());
                    return dto;
                })
                .toList();
    }

    @Override
    public BookingResponseDTO saveBooking(Long tenantId, Long propertyId, BookingDTO bookingDTO) {

        TenantProfile tenant = tenantRepo.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));
        Property property = propertyRepo.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));
        boolean isBooked = bookingRepo.existsByProperty_PropertyIdAndStatus(propertyId, BookingStatus.CONFIRMED);
        if (isBooked)
            throw new RuntimeException("Property is already booked.");

        OwnerProfile owner = property.getOwner();
        Booking booking = new Booking();
        booking.setTenant(tenant);
        booking.setProperty(property);
        booking.setOwnerProfile(owner);
        booking.setStartDate(bookingDTO.getStartDate());
        booking.setEndDate(bookingDTO.getEndDate());
        booking.setStatus(BookingStatus.PENDING);

        System.out.println(booking);
        bookingRepo.save(booking);
        BookingResponseDTO dto = new BookingResponseDTO();
        dto.setBookingId(booking.getBookingId());
        dto.setStartDate(booking.getStartDate());
        dto.setEndDate(booking.getEndDate());
        dto.setStatus(booking.getStatus().name());
        dto.setProperty(new PropertyDTO(booking.getProperty()));
        dto.setTenant(new TenantDTO(booking.getTenant()));
        System.out.println(dto);
        return dto;
    }

    @Override
    public List<BookingResponseDTO> findBookingsByOwnerId(Long ownerId) {
        List<BookingResponseDTO> dtor = bookingRepo.findByProperty_Owner_OwnerId(ownerId).stream()
                .map(booking -> {
                    BookingResponseDTO dto = new BookingResponseDTO();
                    dto.setBookingId(booking.getBookingId());
                    dto.setStartDate(booking.getStartDate());
                    dto.setEndDate(booking.getEndDate());
                    dto.setStatus(booking.getStatus().name());
                    dto.setProperty(new PropertyDTO(booking.getProperty()));
                    dto.setTenant(new TenantDTO(booking.getTenant()));
                    dto.setPayments(booking.getPayments().stream()
                            .map(payment -> new PaymentDTO(payment))
                            .toList());
                    return dto;
                })
                .toList();
        System.out.println(dtor);
        return dtor;
    }

    @Override
    public Booking updateBookingStatus(Long ownerId, Long bookingId, String status) {
        Booking booking = bookingRepo.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));
        TenantProfile tenant = booking.getTenant();
        BookingStatus newStatus;
        try {
            newStatus = BookingStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid  booking status: " + status);
        }
        Property property = propertyRepo.findByBookingsBookingId(bookingId);
        if (newStatus == BookingStatus.CONFIRMED) {
            tenant.setOwner(ownerRepo.findById(ownerId).orElseThrow());
            property.setStatus(AvailableStatus.BOOKED);

            long months = ChronoUnit.MONTHS.between(booking.getStartDate(), booking.getEndDate());
            if (months <= 0)
                months = 1;

            List<Payment> newPayments = paymentFactory.createMonthlyPayments(
                    booking,
                    null,
                    (int) months,
                    property.getPrice());

            booking.getPayments().clear();
            booking.getPayments().addAll(newPayments);
            booking.setStatus(BookingStatus.CONFIRMED);
        } else {
            tenant.setOwner(null);
            property.setStatus(AvailableStatus.AVAILABLE);
            booking.getPayments().clear();

            booking.setStatus(newStatus);
            System.out.println("Updated Booking : " + booking);

        }
        return bookingRepo.save(booking);
    }

    @Override
    public void deleteExistingBooking(Long ownerId, Long bookingId) {
        bookingRepo.deleteById(bookingId);
    }

    @Override
    public List<BookingResponseDTO> findBookingsByTenantId(Long tenantId) {
        List<BookingResponseDTO> dtor = bookingRepo.findByTenantTenantId(tenantId).stream()
                .map(booking -> {
                    BookingResponseDTO dto = new BookingResponseDTO();
                    dto.setBookingId(booking.getBookingId());
                    dto.setStartDate(booking.getStartDate());
                    dto.setEndDate(booking.getEndDate());
                    dto.setStatus(booking.getStatus().name());
                    dto.setProperty(new PropertyDTO(booking.getProperty()));
                    dto.setTenant(new TenantDTO(booking.getTenant()));
                    dto.setPayments(booking.getPayments().stream()
                            .map(payment -> new PaymentDTO(payment))
                            .toList());
                    return dto;
                })
                .toList();
        System.out.println(dtor);
        return dtor;
    }

    @Override
    public List<BookingResponseDTO> findConfirmedBookingsByTenantId(long tenantId) {
        List<BookingResponseDTO> confirmedBookings = bookingRepo.findAllByTenantTenantId(tenantId).stream().filter(
                booking -> booking.getStatus() == BookingStatus.CONFIRMED)
                .map(booking -> {
                    BookingResponseDTO dto = new BookingResponseDTO();
                    dto.setBookingId(booking.getBookingId());
                    dto.setStartDate(booking.getStartDate());
                    dto.setEndDate(booking.getEndDate());
                    dto.setStatus(booking.getStatus().toString());
                    dto.setTenant(new TenantDTO(booking.getTenant()));
                    dto.setProperty(new PropertyDTO(booking.getProperty()));
                    dto.setPayments(booking.getPayments().stream().map(payment -> new PaymentDTO(payment)).toList());
                    return dto;
                }).toList();    

        return confirmedBookings;
    }

}
