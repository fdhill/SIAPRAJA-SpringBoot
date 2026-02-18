package com.example.siapraja.controller;

import com.example.siapraja.dto.StudentDTO;
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
    public ResponseEntity<ResponData<Iterable<Student>>> findAll() {
        ResponData<Iterable<Student>> responseData = new ResponData<>();
        responseData.setStatus(true);
        responseData.setPayload(studentService.findAll());
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponData<Student>> findById(@PathVariable("id") Long id) {
        ResponData<Student> responseData = new ResponData<>();

        responseData.setStatus(true);
        responseData.setPayload(studentService.findById(id));
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/myprofile")
    public ResponseEntity<ResponData<Student>> findByUserIdLogin(Authentication authentication) {
        ResponData<Student> responseData = new ResponData<>();

        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();

        responseData.setPayload(studentService.findByUserId(userDetails.getUserId()));
        responseData.setStatus(true);
        return ResponseEntity.ok(responseData);
    }

    @PostMapping
    public ResponseEntity<ResponData<Student>> create(@Valid @RequestBody StudentDTO studentDTO, Errors errors) {
        ResponData<Student> responseData = new ResponData<>();

        Student student = modelMapper.map(studentDTO, Student.class);
        responseData.setStatus(true);
        responseData.setPayload(studentService.save(student));
        responseData.getMessage().add("Student created successfully");
        return ResponseEntity.ok(responseData);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponData<Student>> update(@PathVariable("id") Long id,
            @Valid @RequestBody StudentDTO studentDTO) {
        ResponData<Student> responseData = new ResponData<>();

        Student student = modelMapper.map(studentDTO, Student.class);
        responseData.setStatus(true);
        responseData.setPayload(studentService.edit(id, student));
        responseData.getMessage().add("Student updated successfully");
        return ResponseEntity.ok(responseData);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PutMapping("/update-profile")
    public ResponseEntity<ResponData<Student>> updateMyProfile(
            @AuthenticationPrincipal MyUserDetails currentUser,
            @Valid @RequestBody StudentDTO studentDTO) {

        ResponData<Student> responseData = new ResponData<>();

        Student myStudent = currentUser.getProfileAs(Student.class);

        if (myStudent == null) {
            throw new RuntimeException("Profil student not found for this user");
        }

        Student studentData = modelMapper.map(studentDTO, Student.class);

        responseData.setPayload(studentService.edit(myStudent.getId(), studentData));
        responseData.setStatus(true);
        responseData.getMessage().add("Profile updated successfully");

        return ResponseEntity.ok(responseData);
    }
}