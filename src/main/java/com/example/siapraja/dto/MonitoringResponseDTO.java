package com.example.siapraja.dto;

import java.time.LocalDate;

import com.example.siapraja.model.Company;
import com.example.siapraja.model.Student;
import com.example.siapraja.model.Teacher;

public class MonitoringResponseDTO {

    private Long id;

    private Student student;

    private Teacher teacher;

    private Company company;

    private LocalDate startDate;

    private LocalDate endDate;
}
