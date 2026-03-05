package com.example.siapraja.service;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.siapraja.dto.MonitoringRequestDTO;
import com.example.siapraja.dto.SubmissionResponDTO;
import com.example.siapraja.model.Company;
import com.example.siapraja.model.Student;
import com.example.siapraja.model.Submission;
import com.example.siapraja.repository.CompanyRepository;
import com.example.siapraja.repository.StudentRepository;
import com.example.siapraja.repository.SubmissionRepository;

@Service
@Transactional
public class SubmissionService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    SubmissionRepository submissionRepository;

    @Autowired
    MonitoringService monitoringService;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Transactional(readOnly = true)
    public SubmissionResponDTO findById(Long id) {
        return submissionRepository.findById(id)
            .map(submission -> modelMapper.map(submission, SubmissionResponDTO.class))
            .orElseThrow(() -> new RuntimeException("Submission with id " + id + " not found"));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public Iterable<SubmissionResponDTO> findAll() {
        Iterable<Submission> submissions = submissionRepository.findAll();

        return StreamSupport.stream(submissions.spliterator(), false)
            .map(submission -> modelMapper.map(submission, SubmissionResponDTO.class))
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Iterable<SubmissionResponDTO> findByStatus(int status) {
        Iterable<Submission> submissions = submissionRepository.findByStatus(status);

        return StreamSupport.stream(submissions.spliterator(), false)
            .map(submission -> modelMapper.map(submission, SubmissionResponDTO.class))
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public boolean hasActiveSubmission(Long id) {
        if (submissionRepository.hasActiveSubmission(id)) {
            return true;
        }
        return false;
    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('STUDENT') and #studentId == authentication.principal.studentId)")
    public SubmissionResponDTO processApply(Long studentId, Long companyId) {
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));
        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new RuntimeException("Company not found"));

        if (submissionRepository.hasActiveSubmission(student.getId())) {
            throw new RuntimeException("You cannot apply. You have a pending application or you are already accepted.");
        } 
        if (company == null) throw new RuntimeException("Company not found.");
        if (company.getQuota() < 1) {
            throw new RuntimeException("Sorry, this company's quota is full.");
        }

        Submission sub = new Submission();
        sub.setStudent(student);
        sub.setCompany(company);
        sub.setStatus(1);

        return modelMapper.map(submissionRepository.save(sub), SubmissionResponDTO.class);
    }


    @PreAuthorize("hasRole('ADMIN')")
    public void accept(Long idSubmission) {
        Submission sub = submissionRepository.findById(idSubmission)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        Company company = sub.getCompany();
        if(sub.getStatus()!= 1){
            throw new RuntimeException("Cannot accept.");
        }
        if (company.getQuota() < 1) {
            throw new RuntimeException("Cannot accept. Company quota is now exhausted.");
        }

        sub.setStatus(2);

        company.setQuota(company.getQuota() - 1);

        MonitoringRequestDTO monitoringRequestDTO = MonitoringRequestDTO.builder()
                .studentId(sub.getStudent().getId())
                .companyId(sub.getCompany().getId())
                .build();

        monitoringService.save(monitoringRequestDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void reject(Long idSubmission) {
        Submission sub = submissionRepository.findById(idSubmission)
                .orElseThrow(() -> new RuntimeException("Submission not found"));
        
        if(sub.getStatus()!= 1){
            throw new RuntimeException("Cannot reject.");
        }
        sub.setStatus(3);
    }
}
