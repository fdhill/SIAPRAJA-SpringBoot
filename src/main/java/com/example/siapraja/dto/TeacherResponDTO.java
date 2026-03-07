package com.example.siapraja.dto;

import lombok.Data;

@Data
public class TeacherResponDTO {
    Long id;

    private String name;

    private String nip;

    private String address;

    private Character gender;
}
