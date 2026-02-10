package com.example.siapraja.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.siapraja.model.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    public Optional<Teacher> findByUserId(Long userID);
}
