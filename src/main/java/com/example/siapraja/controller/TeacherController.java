package com.example.siapraja.controller;

import com.example.siapraja.dto.TeacherDTO;
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
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private ModelMapper modelMapper;

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

        responseData.setStatus(true);
        responseData.setPayload(teacherService.findById(id));
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/myprofile")
    public ResponseEntity<ResponData<Teacher>> getMyProfile(@AuthenticationPrincipal MyUserDetails currentUser){
        ResponData<Teacher> responData = new ResponData<>();

        responData.setPayload(teacherService.findByUserId(currentUser.getUserId()));
        responData.setStatus(true);
        return ResponseEntity.ok(responData);
    }
    

    @PostMapping
    public ResponseEntity<ResponData<Teacher>> create(@Valid @RequestBody TeacherDTO teacherDTO, Errors errors) {
        ResponData<Teacher> responseData = new ResponData<>();

        Teacher teacher = modelMapper.map(teacherDTO, Teacher.class);
        responseData.setStatus(true);
        responseData.setPayload(teacherService.save(teacher));
        responseData.getMessage().add("Teacher created successfully");
        return ResponseEntity.ok(responseData);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ResponData<Teacher>> update(@PathVariable("id") Long id, @Valid @RequestBody TeacherDTO teacherDTO, Errors errors) {
        ResponData<Teacher> responseData = new ResponData<>();
        
        Teacher teacher = modelMapper.map(teacherDTO, Teacher.class);
        responseData.setStatus(true);
        responseData.setPayload(teacherService.edit(id, teacher));
        responseData.getMessage().add("Teacher updated successfully");
        return ResponseEntity.ok(responseData);
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PutMapping("/update-profile")
    public ResponseEntity<ResponData<Teacher>> updateMyProfile(
            @AuthenticationPrincipal MyUserDetails currentUser,
            @Valid @RequestBody TeacherDTO teacherDTO) {

        ResponData<Teacher> responseData = new ResponData<>();

        Teacher myteacher = currentUser.getProfileAs(Teacher.class);

        if (myteacher == null) {
            throw new RuntimeException("Profil teacher not found for this user");
        }

        Teacher teacherData = modelMapper.map(teacherDTO, Teacher.class);

        responseData.setPayload(teacherService.edit(myteacher.getId(), teacherData));
        responseData.setStatus(true);
        responseData.getMessage().add("Profile updated successfully");

        return ResponseEntity.ok(responseData);
    }
}