package com.fmn.fmn_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fmn.fmn_backend.entity.TenantProfile;
import com.fmn.fmn_backend.entity.User;

@Repository
public interface TenantRepository extends JpaRepository<TenantProfile, Long> {

    List<TenantProfile> findByOwnerOwnerId(Long ownerId);

    TenantProfile findByUser(User user);

    Optional<TenantProfile> findByUser_UserId(Long userId); 

}
