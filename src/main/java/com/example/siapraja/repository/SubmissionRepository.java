package com.example.siapraja.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.siapraja.model.Submission;

public interface SubmissionRepository extends JpaRepository<Submission, Long>{
    Submission findByStudentId(Long studentId);

    Iterable<Submission> findByStatus(int status);

    Optional<Submission> findByStudentIdAndStatus(Long studentId, int status);

    @Query("SELECT COUNT(s) > 0 FROM Submission s WHERE s.student.id = :studentId AND (s.status = 1 OR s.status = 2)")
    boolean hasActiveSubmission(@Param("studentId") Long studentId);
}
