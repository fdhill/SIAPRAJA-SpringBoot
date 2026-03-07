package com.example.siapraja.dto;

import lombok.Data;

@Data
public class CompanyResponDTO {

    Long id;
    
    private String name;

    private String address;

    private String phone;
    
    private Integer quota;
}