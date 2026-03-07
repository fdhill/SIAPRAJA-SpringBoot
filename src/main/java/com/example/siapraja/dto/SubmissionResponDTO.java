package com.example.siapraja.dto;

import com.example.siapraja.model.Company;
import com.example.siapraja.model.Student;

import lombok.Data;

@Data
public class SubmissionResponDTO {
    Long id;

    private Student student;

    private Company company;
}
