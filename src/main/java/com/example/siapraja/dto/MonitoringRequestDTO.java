package com.example.siapraja.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MonitoringRequestDTO {
    @NotNull(message = "ID Student is required")
    private Long studentId;

    @NotNull(message = "ID Company is required")
    private Long companyId;

    private Long teacherId;

    private LocalDate startDate;

    private LocalDate endDate;
}