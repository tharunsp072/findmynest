package com.fmn.fmn_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fmn.fmn_backend.entity.OwnerProfile;
import com.fmn.fmn_backend.entity.Property;

@Repository
public interface PropertyRepository extends JpaRepository<Property,Long>{
    // List<Property> findByOwnerOwnerId(Long ownerId);
     List<Property> findAllByDescription(String description );
        List<Property> findAllByAddress(String address);
        List<Property> findAllByStatus(String status);
        List<Property> findAllByAgeOfBuilding(int age);
           List<Property> findByOwner(OwnerProfile owner);
           List<Property> findByOwner_OwnerId(Long ownerId);
}
