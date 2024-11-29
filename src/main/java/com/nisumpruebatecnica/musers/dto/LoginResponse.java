package com.nisumpruebatecnica.musers.dto;

import lombok.Data;

import java.util.Date;

@Data
public class LoginResponse {
    private String jwtToken;
    private Date lastLogin;
}
