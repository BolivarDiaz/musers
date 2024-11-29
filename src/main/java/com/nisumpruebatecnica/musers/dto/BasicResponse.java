package com.nisumpruebatecnica.musers.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class BasicResponse<T> {
    private String message;
    private int statusCode;
    private T data;
}
