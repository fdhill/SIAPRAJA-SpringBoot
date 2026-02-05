package com.example.siapraja.controller;

import com.example.siapraja.dto.TeacherDTO;
import com.example.siapraja.dto.ResponData;
import com.example.siapraja.model.Teacher;
import com.example.siapraja.service.TeacherService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<ResponData<Teacher>> create(@Valid @RequestBody TeacherDTO teacherDTO, Errors errors) {
        ResponData<Teacher> responseData = new ResponData<>();
        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessage().add(error.getDefaultMessage());
            }
            responseData.setStatus(false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }

        Teacher teacher = modelMapper.map(teacherDTO, Teacher.class);
        responseData.setStatus(true);
        responseData.setPayload(teacherService.save(teacher));
        responseData.getMessage().add("Teacher created successfully");
        return ResponseEntity.ok(responseData);
    }

    @GetMapping
    public ResponseEntity<ResponData<Iterable<Teacher>>> findAll() {
        ResponData<Iterable<Teacher>> responseData = new ResponData<>();
        responseData.setStatus(true);
        responseData.setPayload(teacherService.findAll());
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponData<Teacher>> findById(@PathVariable("id") Long id) {
        ResponData<Teacher> responseData = new ResponData<>();
        Teacher teacher = teacherService.findById(id);
        if (teacher == null) {
            responseData.setStatus(false);
            responseData.getMessage().add("Teacher not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
        }
        responseData.setStatus(true);
        responseData.setPayload(teacher);
        return ResponseEntity.ok(responseData);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponData<Teacher>> update(@PathVariable("id") Long id, @Valid @RequestBody TeacherDTO teacherDTO, Errors errors) {
        ResponData<Teacher> responseData = new ResponData<>();
        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessage().add(error.getDefaultMessage());
            }
            responseData.setStatus(false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }

        try {
            Teacher teacher = modelMapper.map(teacherDTO, Teacher.class);
            responseData.setStatus(true);
            responseData.setPayload(teacherService.edit(id, teacher));
            responseData.getMessage().add("Teacher updated successfully");
            return ResponseEntity.ok(responseData);
        } catch (RuntimeException e) {
            responseData.setStatus(false);
            responseData.getMessage().add(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
        }
    }
}