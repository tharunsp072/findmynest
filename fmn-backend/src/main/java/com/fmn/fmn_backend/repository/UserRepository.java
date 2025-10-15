package com.fmn.fmn_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fmn.fmn_backend.entity.User;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByUsername(String name);
    Optional<User> findByUsernameOrEmail(String usernameOrEmail, String usernameOrEmail2);
}
