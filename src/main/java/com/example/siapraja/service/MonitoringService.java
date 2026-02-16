package com.example.siapraja.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.siapraja.model.Monitoring;
import com.example.siapraja.repository.MonitoringRepository;

@Service
@Transactional
public class MonitoringService {

    @Autowired
    private MonitoringRepository monitoringRepository;

    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.userId")
    @Transactional(readOnly = true)
    public Monitoring findById(Long id) {
        return monitoringRepository.findById(id).orElse(null);
    }
    
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public Iterable<Monitoring> findAll() {
        return monitoringRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN') or #studentId == authentication.principal.userId")
    public Monitoring findByStudentId(Long studentId) {
        return monitoringRepository.findByStudentId(studentId).orElse(null);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN') or #teacherId == authentication.principal.userId")
    public Iterable<Monitoring> findByTeacherId(Long teacherId) {
        return monitoringRepository.findByTeacherId(teacherId);
    }
    
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN') or #companyId == authentication.principal.userId")
    public Iterable<Monitoring> findByCompanyId(Long companyId) {
        return monitoringRepository.findByCompanyId(companyId);
    }
    
    public Monitoring save(Monitoring monitoring) {
        return monitoringRepository.save(monitoring);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Monitoring update(Long id, Monitoring monitoring) {
        Monitoring existing = monitoringRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Monitoring dengan ID " + id + " tidak ditemukan"));

        if (monitoring.getStudent() != null) {
            existing.setStudent(monitoring.getStudent());
        }
        if (monitoring.getTeacher() != null) {
            existing.setTeacher(monitoring.getTeacher());
        }
        if (monitoring.getCompany() != null) {
            existing.setCompany(monitoring.getCompany());
        }

        if (monitoring.getStartDate() != null) {
            existing.setStartDate(monitoring.getStartDate());
        }
        if (monitoring.getEndDate() != null) {
            existing.setEndDate(monitoring.getEndDate());
        }
        return monitoringRepository.save(existing);
    }

    @PreAuthorize("hasRole('ADMIN') or #studentId == authentication.principal.userId")
    public void setMonitoringStartDate(Long id, LocalDate startDate) {
        Monitoring monitoring = findById(id);
        if (monitoring.getStartDate() == null) {
            monitoring.setStartDate(startDate);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteById(Long id) {
        monitoringRepository.deleteById(id);
    }
}