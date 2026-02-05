package com.example.siapraja.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.siapraja.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

}
