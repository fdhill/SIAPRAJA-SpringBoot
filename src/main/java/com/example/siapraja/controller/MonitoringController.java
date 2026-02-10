package com.example.siapraja.controller;

import com.example.siapraja.dto.MonitoringDTO;
import com.example.siapraja.dto.ResponData;
import com.example.siapraja.model.Company;
import com.example.siapraja.model.Monitoring;
import com.example.siapraja.model.Student;
import com.example.siapraja.service.MonitoringService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/monitorings")
public class MonitoringController {

    @Autowired
    private MonitoringService monitoringService;

    @PostMapping
    public ResponseEntity<ResponData<Monitoring>> create(@Valid @RequestBody MonitoringDTO monitoringDTO,
            Errors errors) {
        ResponData<Monitoring> responseData = new ResponData<>();

        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessage().add(error.getDefaultMessage());
            }
            responseData.setStatus(false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }

        try {
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

        } catch (RuntimeException e) {
            responseData.setStatus(false);
            responseData.getMessage().add(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        } catch (Exception e) {
            responseData.setStatus(false);
            responseData.getMessage().add("An unexpected error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
    }

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
        Monitoring monitoring = monitoringService.findById(id);

        if (monitoring == null) {
            responseData.setStatus(false);
            responseData.getMessage().add("Monitroing with ID " + id + " not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
        }

        responseData.setStatus(true);
        responseData.setPayload(monitoring);
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ResponData<Monitoring>> findByStudentId(@PathVariable("studentId") Long studentId) {
        ResponData<Monitoring> responseData = new ResponData<>();
        Monitoring monitoring = monitoringService.findByStudentId(studentId);

        if (monitoring == null) {
            responseData.setStatus(false);
            responseData.getMessage().add("Monitoring not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
        }
        responseData.setStatus(true);
        responseData.setPayload(monitoring);
        responseData.getMessage().add("Monitoring data retrieved successfully");
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<ResponData<Iterable<Monitoring>>> findByTeacherId(@PathVariable("teacherId") Long teacherId) {
        ResponData<Iterable<Monitoring>> responseData = new ResponData<>();
        Iterable<Monitoring> data = monitoringService.findByTeacherId(teacherId);

        responseData.setPayload(data);
        responseData.setStatus(true);
        
        if (!data.iterator().hasNext()) {
            responseData.getMessage().add("No monitoring records found for this teacher.");
        } else {
            responseData.getMessage().add("Monitoring data retrieved successfully");
        }

        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<ResponData<Iterable<Monitoring>>> findByCompanyId(@PathVariable("companyId") Long companyId) {
        ResponData<Iterable<Monitoring>> responseData = new ResponData<>();
        Iterable<Monitoring> data = monitoringService.findByCompanyId(companyId);

        responseData.setPayload(data);
        responseData.setStatus(true);
        
        if (!data.iterator().hasNext()) {
            responseData.getMessage().add("No monitoring records found for this company.");
        } else {
            responseData.getMessage().add("Monitoring data retrieved successfully");
        }

        return ResponseEntity.ok(responseData);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponData<Monitoring>> update(@PathVariable("id") Long id, @RequestBody Monitoring monitoring) {
        ResponData<Monitoring> responseData = new ResponData<>();
        try {
            responseData.setStatus(true);
            responseData.setPayload(monitoringService.update(id, monitoring));
            responseData.getMessage().add("Monitoring updated successfully");
            return ResponseEntity.ok(responseData);
        } catch (RuntimeException e) {
            responseData.setStatus(false);
            responseData.getMessage().add(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        monitoringService.deleteById(id);
    }
}