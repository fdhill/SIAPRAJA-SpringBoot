package com.example.siapraja.controller;

import com.example.siapraja.dto.MonitoringDTO;
import com.example.siapraja.dto.ResponData;
import com.example.siapraja.model.Company;
import com.example.siapraja.model.Monitoring;
import com.example.siapraja.model.Student;
import com.example.siapraja.security.MyUserDetails;
import com.example.siapraja.service.MonitoringService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/monitorings")
public class MonitoringController {

    @Autowired
    private MonitoringService monitoringService;

    @GetMapping
    public ResponseEntity<ResponData<Iterable<Monitoring>>> findAll() {
        ResponData<Iterable<Monitoring>> responseData = new ResponData<>();

        responseData.setStatus(true);
        responseData.setPayload(monitoringService.findAll());
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponData<Monitoring>> findById(@PathVariable("id") Long id) {
        ResponData<Monitoring> responseData = new ResponData<>();

        responseData.setStatus(true);
        responseData.setPayload(monitoringService.findById(id));
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/mymonitoring")
    public ResponseEntity<ResponData<Iterable<Monitoring>>> findByUserId(Authentication authentication) {
        ResponData<Iterable<Monitoring>> responseData = new ResponData<>();

        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();

        responseData.setStatus(true);
        responseData.setPayload(monitoringService.findMyMonitoring(userDetails.getUserId()));
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/student/mymonitoring")
    public ResponseEntity<ResponData<Monitoring>> findByStudentId(@AuthenticationPrincipal MyUserDetails currentUser) {
        ResponData<Monitoring> responseData = new ResponData<>();
        Monitoring monitoring = monitoringService.findByStudentId(currentUser.getStudentId());

        responseData.setStatus(true);
        responseData.setPayload(monitoring);
        responseData.getMessage().add("Monitoring data retrieved successfully");
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/teacher/mymonitoring")
    public ResponseEntity<ResponData<Iterable<Monitoring>>> findByTeacherId(@AuthenticationPrincipal MyUserDetails currentUser) {
        ResponData<Iterable<Monitoring>> responseData = new ResponData<>();
        Iterable<Monitoring> data = monitoringService.findByTeacherId(currentUser.getTeacherId());

        responseData.setPayload(data);
        responseData.setStatus(true);
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/company/mymonitoring")
    public ResponseEntity<ResponData<Iterable<Monitoring>>> findByCompanyId(@AuthenticationPrincipal MyUserDetails currentUser) {
        ResponData<Iterable<Monitoring>> responseData = new ResponData<>();
        Iterable<Monitoring> data = monitoringService.findByCompanyId(currentUser.getCompanyId());

        responseData.setPayload(data);
        responseData.setStatus(true);
        return ResponseEntity.ok(responseData);
    }

    @PostMapping
    public ResponseEntity<ResponData<Monitoring>> create(@Valid @RequestBody MonitoringDTO monitoringDTO) {
        ResponData<Monitoring> responseData = new ResponData<>();

        Monitoring monitoring = new Monitoring();

        Student student = new Student();
        student.setId(monitoringDTO.getStudentId());
        monitoring.setStudent(student);

        Company company = new Company();
        company.setId(monitoringDTO.getCompanyId());
        monitoring.setCompany(company);

        Monitoring savedMonitoring = monitoringService.save(monitoring);

        responseData.setStatus(true);
        responseData.setPayload(savedMonitoring);
        responseData.getMessage().add("Monitoring record created successfully");
        return ResponseEntity.ok(responseData);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponData<Monitoring>> update(@PathVariable("id") Long id, @RequestBody Monitoring monitoring) {
        ResponData<Monitoring> responseData = new ResponData<>();
        
        responseData.setStatus(true);
        responseData.setPayload(monitoringService.update(id, monitoring));
        responseData.getMessage().add("Monitoring updated successfully");
        return ResponseEntity.ok(responseData);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        monitoringService.deleteById(id);
    }
}