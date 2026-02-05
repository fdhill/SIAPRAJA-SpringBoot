package com.example.siapraja.dto;

import jakarta.validation.constraints.NotBlank;

public class StudentDTO {
    
    @NotBlank(message = "Student name is required")
    private String name;

    @NotBlank(message = "Student nisn is required")
    private String nisn;

    @NotBlank(message = "Student address is required")
    private String address;

    @NotBlank(message = "Student classroom is required")
    private String classroom;

    private Character gender;
}
