package com.fmn.fmn_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fmn.fmn_backend.entity.OwnerProfile;
import com.fmn.fmn_backend.entity.User;

@Repository
public interface OwnerRepository extends JpaRepository<OwnerProfile, Long> {
    Optional<OwnerProfile> findByUser(User user);

    Optional<OwnerProfile> findByUser_UserId(Long user_id);

}
