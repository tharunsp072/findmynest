package com.fmn.fmn_backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum Role {
    OWNER,
    TENANT,
    ADMIN
}