package com.fmn.fmn_backend.dto;

import com.fmn.fmn_backend.model.Role;

import lombok.Data;


@Data
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private String role;
}
