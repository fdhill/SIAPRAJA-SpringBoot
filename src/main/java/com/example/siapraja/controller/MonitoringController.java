package com.example.siapraja.controller;

import com.example.siapraja.dto.MonitoringRequestDTO;
import com.example.siapraja.dto.MonitoringResponseDTO;
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
    public ResponseEntity<ResponData<Iterable<MonitoringResponseDTO>>> findAll() {
        ResponData<Iterable<MonitoringResponseDTO>> responseData = new ResponData<>();

        responseData.setStatus(true);
        responseData.setPayload(monitoringService.findAll());
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponData<MonitoringResponseDTO>> findById(@PathVariable("id") Long id) {
        ResponData<MonitoringResponseDTO> responseData = new ResponData<>();

        responseData.setStatus(true);
        responseData.setPayload(monitoringService.findById(id));
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/mymonitoring")
    public ResponseEntity<ResponData<Iterable<MonitoringResponseDTO>>> findByUserId(Authentication authentication) {
        ResponData<Iterable<MonitoringResponseDTO>> responseData = new ResponData<>();

        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();

        responseData.setStatus(true);
        responseData.setPayload(monitoringService.findMyMonitoring(userDetails.getUserId()));
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/student/mymonitoring")
    public ResponseEntity<ResponData<MonitoringResponseDTO>> findByStudentId(@AuthenticationPrincipal MyUserDetails currentUser) {
        ResponData<MonitoringResponseDTO> responseData = new ResponData<>();

        responseData.setStatus(true);
        responseData.setPayload(monitoringService.findByStudentId(currentUser.getStudentId()));
        responseData.getMessage().add("Monitoring data retrieved successfully");
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/teacher/mymonitoring")
    public ResponseEntity<ResponData<Iterable<MonitoringResponseDTO>>> findByTeacherId(@AuthenticationPrincipal MyUserDetails currentUser) {
        ResponData<Iterable<MonitoringResponseDTO>> responseData = new ResponData<>();

        responseData.setPayload(monitoringService.findByTeacherId(currentUser.getTeacherId()));
        responseData.setStatus(true);
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/company/mymonitoring")
    public ResponseEntity<ResponData<Iterable<MonitoringResponseDTO>>> findByCompanyId(@AuthenticationPrincipal MyUserDetails currentUser) {
        ResponData<Iterable<MonitoringResponseDTO>> responseData = new ResponData<>();

        responseData.setPayload(monitoringService.findByCompanyId(currentUser.getCompanyId()));
        responseData.setStatus(true);
        return ResponseEntity.ok(responseData);
    }

    @PostMapping
    public ResponseEntity<ResponData<MonitoringResponseDTO>> create(@Valid @RequestBody MonitoringRequestDTO monitoringRequestDTO) {
        ResponData<MonitoringResponseDTO> responseData = new ResponData<>();

        responseData.setStatus(true);
        responseData.setPayload(monitoringService.save(monitoringRequestDTO));
        responseData.getMessage().add("Monitoring record created successfully");
        return ResponseEntity.ok(responseData);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponData<MonitoringResponseDTO>> update(@PathVariable("id") Long id, @RequestBody MonitoringRequestDTO monitoringRequestDTO) {
        ResponData<MonitoringResponseDTO> responseData = new ResponData<>();
        
        responseData.setStatus(true);
        responseData.setPayload(monitoringService.update(id, monitoringRequestDTO));
        responseData.getMessage().add("Monitoring updated successfully");
        return ResponseEntity.ok(responseData);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        monitoringService.deleteById(id);
    }
}