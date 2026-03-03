package com.example.siapraja.service;

import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.asm.Advice.Return;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.siapraja.dto.MonitoringResponseDTO;
import com.example.siapraja.model.Monitoring;
import com.example.siapraja.repository.MonitoringRepository;

@Service
@Transactional
public class MonitoringService {

    private final ModelMapper modelMapper;

    @Autowired
    private MonitoringRepository monitoringRepository;

    MonitoringService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public Iterable<MonitoringResponseDTO> findAll() {
        Iterable<Monitoring> monitorings = monitoringRepository.findAll();
        
        return StreamSupport.stream(monitorings.spliterator(), false)
            .map(monitoring -> modelMapper.map(monitoring, MonitoringResponseDTO.class))
            .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public MonitoringResponseDTO findById(Long id) {
        return monitoringRepository.findById(id)
            .map(monitoring -> modelMapper.map(monitoring, MonitoringResponseDTO.class))
            .orElseThrow(() -> new RuntimeException("Monitoring with id " + id + "not found"));
    } 

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN') or (hasRole('STUDENT') and #studentId == authentication.principal.studentId)")
    public MonitoringResponseDTO findByStudentId(Long studentId) {
        return monitoringRepository.findByStudentId(studentId)
            .map(monitoring -> modelMapper.map(monitoring, MonitoringResponseDTO.class))
            .orElseThrow(() -> new RuntimeException("Monitoring with studnet id " + studentId + "not found"));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN') or (hasRole('TEACHER') and #teacherId == authentication.principal.teacherId)")
    public Iterable<MonitoringResponseDTO> findByTeacherId(Long teacherId) {
        Iterable<Monitoring> monitorings = monitoringRepository.findByTeacherId(teacherId);

        return StreamSupport.stream(monitorings.spliterator(), false)
            .map(monitoring -> modelMapper.map(monitorings, MonitoringResponseDTO.class))
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN') or (hasRole('COMPANY') and #companyId == authentication.principal.companyId)")
    public Iterable<MonitoringResponseDTO> findByCompanyId(Long companyId) {
        Iterable<Monitoring> monitorings = monitoringRepository.findByCompanyId(companyId);

        return StreamSupport.stream(monitorings.spliterator(), false)
            .map(monitoring -> modelMapper.map(monitorings, MonitoringResponseDTO.class))
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @PreAuthorize("#id == authentication.principal.userId")
    public Iterable<MonitoringResponseDTO> findMyMonitoring(Long userId) {
        return monitoringRepository.findByStudent_User_IdOrTeacher_User_IdOrCompany_User_Id(userId, userId, userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public MonitoringResponseDTO save(Monitoring monitoring) {
        return monitoringRepository.save(monitoring);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public MonitoringResponseDTO update(Long id, Monitoring monitoring) {
        Monitoring existing = monitoringRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Monitoring with ID " + id + " not found"));

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

    @PreAuthorize("hasRole('ADMIN') or (hasRole('STUDENT') and #studentId == authentication.principal.studentId)")
    public void setMonitoringStartDate(Long studentId, LocalDate startDate) {
        Monitoring monitoring = findByStudentId(studentId);
        if (monitoring.getStartDate() == null) {
            monitoring.setStartDate(startDate);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteById(Long id) {
        monitoringRepository.deleteById(id);
    }
}