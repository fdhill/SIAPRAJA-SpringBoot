package com.example.siapraja.dto;

import jakarta.validation.constraints.NotBlank;

public class UserResponDTO {
    Long id;

    @NotBlank(message = "name name is required")
    private String name;

    @NotBlank(message = "Username is required")
    private String username;

}
