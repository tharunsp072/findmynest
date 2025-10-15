package com.fmn.fmn_backend.dto;


import lombok.Data;

@Data
public class UserDTO {
    private String usernameOrEmail;
    private String password;
}
