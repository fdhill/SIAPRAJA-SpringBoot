package com.example.siapraja.controller;

import com.example.siapraja.dto.StudentDTO;
import com.example.siapraja.dto.StudentRequestDTO;
import com.example.siapraja.dto.StudentRespontDTO;
import com.example.siapraja.dto.ResponData;
import com.example.siapraja.model.Student;
import com.example.siapraja.security.MyUserDetails;
import com.example.siapraja.service.StudentService;
import jakarta.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<ResponData<Iterable<StudentRespontDTO>>> findAll() {
        ResponData<Iterable<StudentRespontDTO>> responseData = new ResponData<>();
        responseData.setStatus(true);
        responseData.setPayload(studentService.findAll());
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponData<StudentRespontDTO>> findById(@PathVariable("id") Long id) {
        ResponData<StudentRespontDTO> responseData = new ResponData<>();

        responseData.setStatus(true);
        responseData.setPayload(studentService.findById(id));
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/myprofile")
    public ResponseEntity<ResponData<StudentRespontDTO>> getMyProfile(Authentication authentication) {
        ResponData<StudentRespontDTO> responseData = new ResponData<>();

        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();

        responseData.setPayload(studentService.findByUserId(userDetails.getUserId()));
        responseData.setStatus(true);
        return ResponseEntity.ok(responseData);
    }

    @PostMapping
    public ResponseEntity<ResponData<StudentRespontDTO>> create(@Valid @RequestBody StudentRequestDTO studentRequestDTO) {
        ResponData<StudentRespontDTO> responseData = new ResponData<>();

        responseData.setStatus(true);
        responseData.setPayload(studentService.save(studentRequestDTO));
        responseData.getMessage().add("Student created successfully");
        return ResponseEntity.ok(responseData);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponData<StudentRespontDTO>> update(@PathVariable("id") Long id, @Valid @RequestBody StudentRequestDTO studentRequestDTO) {
        ResponData<StudentRespontDTO> responseData = new ResponData<>();

        responseData.setStatus(true);
        responseData.setPayload(studentService.edit(id, studentRequestDTO));
        responseData.getMessage().add("Student updated successfully");
        return ResponseEntity.ok(responseData);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PutMapping("/update-profile")
    public ResponseEntity<ResponData<StudentRespontDTO>> updateMyProfile( @AuthenticationPrincipal MyUserDetails currentUser, @Valid @RequestBody StudentRequestDTO studentRequestDTO) {

        ResponData<StudentRespontDTO> responseData = new ResponData<>();

        Student myStudent = currentUser.getProfileAs(Student.class);

        if (myStudent == null) {
            throw new RuntimeException("Profil student not found for this user");
        }

        responseData.setPayload(studentService.edit(myStudent.getId(), studentRequestDTO));
        responseData.setStatus(true);
        responseData.getMessage().add("Profile updated successfully");

        return ResponseEntity.ok(responseData);
    }
}