package com.fmn.fmn_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.fmn.fmn_backend.config.JwtUtils;
import com.fmn.fmn_backend.dto.JwtResponse;
import com.fmn.fmn_backend.dto.LoginRequest;
import com.fmn.fmn_backend.dto.RegisterRequest;
import com.fmn.fmn_backend.entity.OwnerProfile;
import com.fmn.fmn_backend.entity.TenantProfile;
import com.fmn.fmn_backend.entity.User;
import com.fmn.fmn_backend.repository.OwnerRepository;
import com.fmn.fmn_backend.repository.TenantRepository;
import com.fmn.fmn_backend.repository.UserRepository;
import com.fmn.fmn_backend.service.AuthService.AuthService;

@CrossOrigin(origins = "http://localhost:3333")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private TenantRepository tenantRepository;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @PostMapping("/login")
    public JwtResponse login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmailorUsername(),
                        loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername()).get();

        String role = user.getRole().iterator().next().name();
        String token = jwtUtils.generateToken(user.getUsername(), role, user.getUserId());

        // Fetch profileId based on role
        Long profileId = null;
        if (role.equalsIgnoreCase("TENANT")) {
            TenantProfile tenant = tenantRepository.findByUser_UserId(user.getUserId()).orElse(null);
            if (tenant != null)
                profileId = tenant.getTenantId();
        } else if (role.equalsIgnoreCase("OWNER")) {
            OwnerProfile owner = ownerRepository.findByUser_UserId(user.getUserId()).orElse(null);
            if (owner != null)
                profileId = owner.getOwnerId();
        }

        return new JwtResponse(token, user.getUserId(), user.getUsername(), user.getRole(), profileId);
    }

}
