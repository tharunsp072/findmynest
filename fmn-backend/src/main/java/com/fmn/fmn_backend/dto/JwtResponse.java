package com.fmn.fmn_backend.dto;

import java.util.Set;

import com.fmn.fmn_backend.model.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {

    private String token;
    private Long userId;
    private String username;
    private Set<Role> roles;
    private Long profileId; 
}
