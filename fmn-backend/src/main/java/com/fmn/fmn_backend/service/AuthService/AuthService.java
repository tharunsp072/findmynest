package com.fmn.fmn_backend.service.AuthService;

import java.util.Collections;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fmn.fmn_backend.config.JwtUtils;
import com.fmn.fmn_backend.dto.JwtResponse;
import com.fmn.fmn_backend.dto.LoginRequest;
import com.fmn.fmn_backend.dto.RegisterRequest;
import com.fmn.fmn_backend.entity.OwnerProfile;
import com.fmn.fmn_backend.entity.TenantProfile;
import com.fmn.fmn_backend.entity.User;
import com.fmn.fmn_backend.model.Role;
import com.fmn.fmn_backend.repository.OwnerRepository;
import com.fmn.fmn_backend.repository.TenantRepository;
import com.fmn.fmn_backend.repository.UserRepository;

@Service
public class AuthService {
    private final UserRepository userRepo;
    private final TenantRepository tenantRepository;
    private final PasswordEncoder passwordEncoder;
    private final OwnerRepository ownerRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthService(UserRepository userRepo, TenantRepository tenantRepository, PasswordEncoder passwordEncoder,
            OwnerRepository ownerRepository, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.userRepo = userRepo;
        this.tenantRepository = tenantRepository;
        this.passwordEncoder = passwordEncoder;
        this.ownerRepository = ownerRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    public String register(RegisterRequest registerRequest) {
        if (userRepo.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        Role role = Role.valueOf(registerRequest.getRole().toUpperCase());
        user.setRole(Collections.singleton(role));
        userRepo.save(user);

        if (role == Role.TENANT) {
            TenantProfile tenantProfile = new TenantProfile();
            tenantProfile.setUser(user);
            tenantProfile.setUsername(registerRequest.getUsername());
            tenantRepository.save(tenantProfile);
        } else if (role == Role.OWNER) {
            OwnerProfile ownerProfile = new OwnerProfile();
            ownerProfile.setUser(user);
            ownerProfile.setFullname(registerRequest.getUsername());
            ownerProfile.setTotal_revenue("0");
            ownerRepository.save(ownerProfile);
        }

        return "User registered successfully";
    }

    public JwtResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmailorUsername(),
                        loginRequest.getPassword()));

        User user = userRepo.findByUsernameOrEmail(
                loginRequest.getEmailorUsername(),
                loginRequest.getEmailorUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String role = user.getRole().iterator().next().name();
        String jwt = jwtUtils.generateToken(user.getUsername(), role, user.getUserId());

        Long profileId = null;
        if (role.equals("TENANT")) {
            TenantProfile tenant = tenantRepository.findByUser(user);
            profileId = tenant.getTenantId();
        } else if (role.equals("OWNER")) {
            OwnerProfile owner = ownerRepository.findByUser(user)
                    .orElseThrow(() -> new RuntimeException("Owner profile not found"));
            profileId = owner.getOwnerId();
        }

        return new JwtResponse(jwt, user.getUserId(), user.getUsername(), user.getRole(), profileId);
    }
}
