package com.nisumpruebatecnica.musers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveUserResponse {
    private Integer id;
    private Date created;
    private Date modified;
    private Date lastLogin;
    private Boolean isActive;
    private String jwtToken;
}
