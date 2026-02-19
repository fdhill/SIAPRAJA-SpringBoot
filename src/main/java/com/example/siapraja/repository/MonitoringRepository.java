package com.example.siapraja.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.siapraja.model.Monitoring;

public interface MonitoringRepository extends JpaRepository<Monitoring, Long> {
    Optional<Monitoring> findByStudentId(Long studentId);

    Iterable<Monitoring> findByTeacherId(Long teacherId);

    Iterable<Monitoring> findByCompanyId(Long companyId);

    Optional<Monitoring> findByStudent_User_Id(Long userId);

    Optional<Monitoring> findByTeacher_User_Id(Long userId);

    Optional<Monitoring> findByCompany_User_Id(Long userId);

    Iterable<Monitoring> findByStudent_User_IdOrTeacher_User_IdOrCompany_User_Id(
            Long studentUserId,
            Long teacherUserId,
            Long companyUserId);
}
