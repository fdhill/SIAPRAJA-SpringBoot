package com.example.siapraja.controller;

import com.example.siapraja.dto.StudentDTO;
import com.example.siapraja.dto.ResponData;
import com.example.siapraja.model.Student;
import com.example.siapraja.service.StudentService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<ResponData<Student>> create(@Valid @RequestBody StudentDTO studentDTO, Errors errors) {
        ResponData<Student> responseData = new ResponData<>();
        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessage().add(error.getDefaultMessage());
            }
            responseData.setStatus(false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }

        Student student = modelMapper.map(studentDTO, Student.class);
        responseData.setStatus(true);
        responseData.setPayload(studentService.save(student));
        responseData.getMessage().add("Student created successfully");
        return ResponseEntity.ok(responseData);
    }

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
        Student student = studentService.findById(id);
        if (student == null) {
            responseData.setStatus(false);
            responseData.getMessage().add("Student not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
        }
        responseData.setStatus(true);
        responseData.setPayload(student);
        return ResponseEntity.ok(responseData);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponData<Student>> update(@PathVariable("id") Long id, @Valid @RequestBody StudentDTO studentDTO, Errors errors) {
        ResponData<Student> responseData = new ResponData<>();
        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessage().add(error.getDefaultMessage());
            }
            responseData.setStatus(false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }

        try {
            Student student = modelMapper.map(studentDTO, Student.class);
            responseData.setStatus(true);
            responseData.setPayload(studentService.edit(id, student));
            responseData.getMessage().add("Student updated successfully");
            return ResponseEntity.ok(responseData);
        } catch (RuntimeException e) {
            responseData.setStatus(false);
            responseData.getMessage().add(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
        }
    }
}