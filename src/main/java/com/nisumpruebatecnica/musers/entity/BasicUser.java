package com.nisumpruebatecnica.musers.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.Map;
@Entity
@Table(name = "musers")
@Data
public class BasicUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String username;
    private String name;
    private String email;
    private String password;
    private String jsonPhones;
    private Date created;
    private Date modified;
    private Date lastLogin;
    private String jwt;
    private Boolean isActive;
}
