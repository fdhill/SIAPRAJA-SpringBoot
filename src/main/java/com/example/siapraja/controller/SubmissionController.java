package com.example.siapraja.controller;

import java.util.Collections;

import org.modelmapper.ModelMapper;
import org.springframework.validation.Errors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.siapraja.dto.ResponData;
import com.example.siapraja.dto.SubmissionDTO;
import com.example.siapraja.model.Submission;
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

    @PostMapping("/apply")
    public ResponseEntity<ResponData<Submission>> create(
            @Valid @RequestBody SubmissionDTO submissionDTO,
            @AuthenticationPrincipal MyUserDetails currentUser,
            Errors errors) {

        ResponData<Submission> responseData = new ResponData<>();

        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessage().add(error.getDefaultMessage());
            }
            responseData.setStatus(false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }

        try {
            Long userId = currentUser.getUserId();

            Submission result = submissionService.processApply(userId, submissionDTO.getCompanyId());

            responseData.setStatus(true);
            responseData.setPayload(result);
            responseData.getMessage().add("Application submitted successfully");
            return ResponseEntity.ok(responseData);

        } catch (RuntimeException e) {
            responseData.setStatus(false);
            responseData.getMessage().add(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
    }

    @GetMapping
    public ResponseEntity<ResponData<Iterable<Submission>>> findAll() {
        ResponData<Iterable<Submission>> response = new ResponData<>();
        response.setStatus(true);
        response.setPayload(submissionService.findAll());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ResponData<Iterable<Submission>>> findByStatus(@PathVariable("status") int status) {
        ResponData<Iterable<Submission>> response = new ResponData<>();
        response.setStatus(true);
        response.setPayload(submissionService.findByStatus(status));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<ResponData<String>> accept(@PathVariable Long id) {
        ResponData<String> response = new ResponData<>();
        try {
            submissionService.accept(id);
            response.setStatus(true);
            response.setMessage(Collections.singletonList("Submission accepted"));
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.setStatus(false);
            response.setMessage(Collections.singletonList(e.getMessage()));
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<ResponData<String>> reject(@PathVariable Long id) {
        ResponData<String> response = new ResponData<>();
        try {
            submissionService.reject(id);
            response.setStatus(true);
            response.setMessage(Collections.singletonList("Submission reject."));
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.setStatus(false);
            response.setMessage(Collections.singletonList(e.getMessage()));
            return ResponseEntity.badRequest().body(response);
        }
    }
}
