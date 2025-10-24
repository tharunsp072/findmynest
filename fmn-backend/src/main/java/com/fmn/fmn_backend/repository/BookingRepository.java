package com.fmn.fmn_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fmn.fmn_backend.dto.BookingDTO.BookingResponseDTO;
import com.fmn.fmn_backend.entity.Booking;
import com.fmn.fmn_backend.model.BookingStatus;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long>{

    List<Booking> findByTenantTenantId(Long tenantId);

    boolean existsByProperty_PropertyIdAndStatus(Long propertyId, BookingStatus status);

    List<Booking> findByProperty_Owner_OwnerId(Long ownerId);

    List<Booking> findAllByTenantTenantId(long tenantId);

    // Booking findByOwnerOwnerId(Long ownerId);

    // List<Booking> findByProperty_Owner_OwnerId(Long ownerId);
    // List<Booking> findByOwnerOwnerId(Long ownerId);
    
}
