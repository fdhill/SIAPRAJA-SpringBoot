package com.example.siapraja.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRequestDTO {
    Long id;

    @NotBlank(message = "name name is required")
    private String name;

    @NotBlank(message = "Username is required")
    private String username;

    private String password;
    
    private Integer role;
}
