package com.example.siapraja.dto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubmissionDTO {
    // @NotNull(message = "ID Student is required")
    // private Long studentId;

    @NotNull(message = "ID Company is required")
    private Long companyId;
}
