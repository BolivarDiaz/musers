package com.nisumpruebatecnica.musers.dto;

import lombok.Data;

import java.util.List;

@Data
public class SaveUserRequest {
    private String username;
    private String name;
    private String email;
    private String password;
    private List<Phone> phones;
    private String existingJwt;
}
