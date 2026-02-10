package com.example.siapraja.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PresenceDTO {
    
    @NotBlank(message = "Locaation is required")
    private String location;

    private String notes;

    @NotNull(message = "Status is required")
    private Integer status;
}