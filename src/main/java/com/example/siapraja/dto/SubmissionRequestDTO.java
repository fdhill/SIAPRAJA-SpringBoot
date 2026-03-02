package com.example.siapraja.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubmissionRequestDTO {

    @NotNull(message = "ID Company is required")
    private Long companyId;
    
}