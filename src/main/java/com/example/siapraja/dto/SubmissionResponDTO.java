package com.example.siapraja.dto;

import com.example.siapraja.model.Company;
import com.example.siapraja.model.Student;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubmissionResponDTO {
    Long id;

    @NotNull(message = "ID Student is required")
    private Student student;

    @NotNull(message = "ID Company is required")
    private Company company;
}
