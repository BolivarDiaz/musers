package com.nisumpruebatecnica.musers.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class GetResponse {
    private String name;
    private String email;
    private String password;
    private List<Phone> phones;
    private Date creationDate;
    private Date lastLogin;
    private Boolean isActive;
}
