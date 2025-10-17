package com.fmn.fmn_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fmn.fmn_backend.dto.PropertyDTO.PropertyDTO;
import com.fmn.fmn_backend.entity.OwnerProfile;
import com.fmn.fmn_backend.entity.Property;
import com.fmn.fmn_backend.model.AvailableStatus;
import com.fmn.fmn_backend.model.BookingStatus;

@Repository
public interface PropertyRepository extends JpaRepository<Property,Long>{
    // List<Property> findByOwnerOwnerId(Long ownerId);
     List<Property> findAllByDescription(String description );
        List<Property> findAllByAddress(String address);
        List<Property> findAllByStatus(AvailableStatus available);
        List<Property> findAllByAgeOfBuilding(int age);
           List<Property> findByOwner(OwnerProfile owner);
           List<Property> findByOwner_OwnerId(Long ownerId);
             @Query("SELECT DISTINCT p FROM Property p JOIN p.bookings b WHERE b.status = :status")
    List<Property> findAllByBookingStatus(@Param("status") BookingStatus status);
             Property findByBookingsBookingId(Long bookingId);
}
