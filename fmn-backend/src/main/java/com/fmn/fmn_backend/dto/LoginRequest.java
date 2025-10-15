package com.fmn.fmn_backend.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String emailorUsername;
    private String password;
}
