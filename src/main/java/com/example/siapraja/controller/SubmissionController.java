package com.example.siapraja.controller;

import java.util.Collections;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.siapraja.dto.ResponData;
import com.example.siapraja.dto.SubmissionRequestDTO;
import com.example.siapraja.dto.SubmissionResponDTO;
import com.example.siapraja.security.MyUserDetails;
import com.example.siapraja.service.SubmissionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {

    @Autowired
    SubmissionService submissionService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<ResponData<Iterable<SubmissionResponDTO>>> findAll() {
        ResponData<Iterable<SubmissionResponDTO>> response = new ResponData<>();
        response.setStatus(true);
        response.setPayload(submissionService.findAll());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ResponData<Iterable<SubmissionResponDTO>>> findByStatus(@PathVariable("status") int status) {
        ResponData<Iterable<SubmissionResponDTO>> response = new ResponData<>();
        response.setStatus(true);
        response.setPayload(submissionService.findByStatus(status));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/apply")
    public ResponseEntity<ResponData<SubmissionResponDTO>> create(@Valid @RequestBody SubmissionRequestDTO submissionRequestDTO, @AuthenticationPrincipal MyUserDetails currentUser) {
        ResponData<SubmissionResponDTO> responseData = new ResponData<>();

        responseData.setStatus(true);
        responseData.setPayload(submissionService.processApply(currentUser.getStudentId(), submissionRequestDTO.getCompanyId()));
        responseData.getMessage().add("Application submitted successfully");
        return ResponseEntity.ok(responseData);
    }

    @PutMapping("/accept/{id}")
    public ResponseEntity<ResponData<String>> accept(@PathVariable Long id) {
        ResponData<String> response = new ResponData<>();

        submissionService.accept(id);
        response.setStatus(true);
        response.setMessage(Collections.singletonList("Submission accepted & making monitoring"));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/reject/{id}")
    public ResponseEntity<ResponData<String>> reject(@PathVariable Long id) {
        ResponData<String> response = new ResponData<>();
        submissionService.reject(id);
        response.setStatus(true);
        response.setMessage(Collections.singletonList("Submission reject."));
        return ResponseEntity.ok(response);
    }
}
