package com.example.siapraja.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.siapraja.model.Monitoring;

public interface MonitoringRepository extends JpaRepository<Monitoring, Long>{
    Optional<Monitoring> findByStudentId(Long studentId);

    Iterable<Monitoring> findByTeacherId(Long teacherId);

    Iterable<Monitoring> findByCompanyId(Long companyId);
}
