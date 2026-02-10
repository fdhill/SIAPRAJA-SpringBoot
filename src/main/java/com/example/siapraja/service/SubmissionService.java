package com.example.siapraja.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.siapraja.model.Company;
import com.example.siapraja.model.Monitoring;
import com.example.siapraja.model.Student;
import com.example.siapraja.model.Submission;
import com.example.siapraja.repository.SubmissionRepository;

@Service
@Transactional
public class SubmissionService {

    @Autowired
    SubmissionRepository submissionRepository;

    @Autowired
    MonitoringService monitoringService;

    @Autowired
    StudentService studentService;

    @Autowired
    CompanyService companyService;

    @Transactional(readOnly = true)
    public Submission findById(Long id) {
        return submissionRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public Iterable<Submission> findAll() {
        return submissionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Iterable<Submission> findByStatus(int status) {
        return submissionRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public boolean hasActiveSubmission(Long id) {
        if (submissionRepository.hasActiveSubmission(id)) {
            return true;
        }
        return false;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT') or #userId == authentication.principal.userId")
    public Submission processApply(Long userId, Long companyId) {
        Student student = studentService.findByUserId(userId);

        if (submissionRepository.hasActiveSubmission(student.getId())) {
            throw new RuntimeException("You cannot apply. You have a pending application or you are already accepted.");
        }
        
        Company company = companyService.findById(companyId);

        if (company == null) throw new RuntimeException("Company not found.");
        if (company.getQuota() < 1) {
            throw new RuntimeException("Sorry, this company's quota is full.");
        }

        Submission sub = new Submission();
        sub.setStudent(student);
        sub.setCompany(company);
        sub.setStatus(1);

        return submissionRepository.save(sub);
    }


    @PreAuthorize("hasRole('ADMIN')")
    public void accept(Long idSubmission) {
        Submission sub = submissionRepository.findById(idSubmission)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        Company company = sub.getCompany();
        if (company.getQuota() < 1) {
            throw new RuntimeException("Cannot accept. Company quota is now exhausted.");
        }

        sub.setStatus(2);

        company.setQuota(company.getQuota() - 1);

        Monitoring monitoring = Monitoring.builder()
                .student(sub.getStudent())
                .company(sub.getCompany())
                .build();

        monitoringService.save(monitoring);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void reject(Long idSubmission) {
        Submission sub = submissionRepository.findById(idSubmission)
                .orElseThrow(() -> new RuntimeException("Submission not found"));
        sub.setStatus(3);
    }
}
