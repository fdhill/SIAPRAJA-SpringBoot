package com.example.siapraja.controller;

import com.example.siapraja.dto.TeacherRequestDTO;
import com.example.siapraja.dto.TeacherResponDTO;
import com.example.siapraja.dto.ResponData;
import com.example.siapraja.model.Teacher;
import com.example.siapraja.security.MyUserDetails;
import com.example.siapraja.service.TeacherService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<ResponData<Iterable<TeacherResponDTO>>> findAll() {
        ResponData<Iterable<TeacherResponDTO>> responseData = new ResponData<>();

        responseData.setStatus(true);
        responseData.setPayload(teacherService.findAll());
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponData<TeacherResponDTO>> findById(@PathVariable("id") Long id) {
        ResponData<TeacherResponDTO> responseData = new ResponData<>();

        responseData.setStatus(true);
        responseData.setPayload(teacherService.findById(id));
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/myprofile")
    public ResponseEntity<ResponData<TeacherResponDTO>> getMyProfile(@AuthenticationPrincipal MyUserDetails currentUser){
        ResponData<TeacherResponDTO> responData = new ResponData<>();

        responData.setPayload(teacherService.findByUserId(currentUser.getUserId()));
        responData.setStatus(true);
        return ResponseEntity.ok(responData);
    }
    

    @PostMapping
    public ResponseEntity<ResponData<TeacherResponDTO>> create(@Valid @RequestBody TeacherRequestDTO teacherRequestDTO) {
        ResponData<TeacherResponDTO> responseData = new ResponData<>();

        responseData.setStatus(true);
        responseData.setPayload(teacherService.save(teacherRequestDTO));
        responseData.getMessage().add("Teacher created successfully");
        return ResponseEntity.ok(responseData);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ResponData<TeacherResponDTO>> update(@PathVariable("id") Long id, @Valid @RequestBody TeacherRequestDTO teacherRequestDTO) {
        ResponData<TeacherResponDTO> responseData = new ResponData<>();
        
        responseData.setStatus(true);
        responseData.setPayload(teacherService.edit(id, teacherRequestDTO));
        responseData.getMessage().add("Teacher updated successfully");
        return ResponseEntity.ok(responseData);
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PutMapping("/update-profile")
    public ResponseEntity<ResponData<TeacherResponDTO>> updateMyProfile(@AuthenticationPrincipal MyUserDetails currentUser, @Valid @RequestBody TeacherRequestDTO teacherRequestDTO) {

        ResponData<TeacherResponDTO> responseData = new ResponData<>();

        Teacher myteacher = currentUser.getProfileAs(Teacher.class);

        if (myteacher == null) {
            throw new RuntimeException("Profil teacher not found for this user");
        }

        responseData.setPayload(teacherService.edit(myteacher.getId(), teacherRequestDTO));
        responseData.setStatus(true);
        responseData.getMessage().add("Profile updated successfully");

        return ResponseEntity.ok(responseData);
    }
}