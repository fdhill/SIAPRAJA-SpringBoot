package com.example.siapraja.service;

import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.siapraja.dto.MonitoringRequestDTO;
import com.example.siapraja.dto.MonitoringResponseDTO;
import com.example.siapraja.model.Company;
import com.example.siapraja.model.Monitoring;
import com.example.siapraja.model.Student;
import com.example.siapraja.model.Teacher;
import com.example.siapraja.repository.MonitoringRepository;

@Service
@Transactional
public class MonitoringService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private MonitoringRepository monitoringRepository;

    @Autowired
    StudentService studentService;

    @Autowired
    CompanyService companyService;

    @Autowired
    TeacherService teacherService;

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
        Iterable<Monitoring> monitorings = monitoringRepository.findByStudent_User_IdOrTeacher_User_IdOrCompany_User_Id(userId, userId, userId);

        return StreamSupport.stream(monitorings.spliterator(), false)
            .map(monitoring -> modelMapper.map(monitorings, MonitoringResponseDTO.class))
            .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN')")
    public MonitoringResponseDTO save(MonitoringRequestDTO monitoringRequestDTO) {

        Monitoring monitoring = Monitoring.builder()
            .student(modelMapper.map(studentService.findById(monitoringRequestDTO.getStudentId()), Student.class))
            .company(modelMapper.map(companyService.findById(monitoringRequestDTO.getCompanyId()), Company.class))
            .build();

        return modelMapper.map(monitoringRepository.save(monitoring), MonitoringResponseDTO.class);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public MonitoringResponseDTO update(Long id, MonitoringRequestDTO monitoringRequestDTO){
        Monitoring existing = monitoringRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Monitoring with ID " + id + " not found"));

        if (monitoringRequestDTO.getStudentId() != null) {
            existing.setStudent(modelMapper.map(studentService.findById(monitoringRequestDTO.getStudentId()), Student.class));
        }
        if (monitoringRequestDTO.getTeacherId() != null) {
            existing.setTeacher(modelMapper.map(teacherService.findById(monitoringRequestDTO.getTeacherId()), Teacher.class));
        }
        if (monitoringRequestDTO.getCompanyId() != null) {
            existing.setCompany(modelMapper.map(companyService.findById(monitoringRequestDTO.getCompanyId()), Company.class));
        }

        if (monitoringRequestDTO.getStartDate() != null) {
            existing.setStartDate(monitoringRequestDTO.getStartDate());
        }
        if (monitoringRequestDTO.getEndDate() != null) {
            existing.setEndDate(monitoringRequestDTO.getEndDate());
        }
        return modelMapper.map(monitoringRepository.save(existing), MonitoringResponseDTO.class);
    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('STUDENT') and #studentId == authentication.principal.studentId)")
    public void setMonitoringStartDate(Long studentId, LocalDate startDate) {
        Monitoring monitoring = monitoringRepository.findByStudentId(studentId)
            .orElseThrow(() -> new RuntimeException("Monitoring with student id " + studentId + "nout found"));
        if (monitoring.getStartDate() == null) {
            monitoring.setStartDate(startDate);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteById(Long id) {
        monitoringRepository.deleteById(id);
    }
}