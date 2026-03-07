package com.example.siapraja.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TeacherRequestDTO {
    Long id;

    @NotBlank(message = "Teacher name is required")
    private String name;

    @NotBlank(message = "Teacher nip is required")
    private String nip;

    @NotBlank(message = "Teacher address is required")
    private String address;

    private Character gender;
}
