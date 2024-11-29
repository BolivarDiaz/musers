package com.nisumpruebatecnica.musers.dto;

import lombok.Data;

import java.util.Date;

@Data
public class SaveUserResponse {
    private Integer id;
    private Date created;
    private Date modified;
    private Date lastLogin;
    private Boolean isActive;
    private String jwtToken;
}
