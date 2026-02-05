package com.example.siapraja.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CompanyDTO {
    
    @NotBlank(message = "Company name is required")
    private String name;

    @NotBlank(message = "Address is required")
    private String address;

    private String phone;
    
    @NotNull(message = "price is required")
    private Integer quota;
}