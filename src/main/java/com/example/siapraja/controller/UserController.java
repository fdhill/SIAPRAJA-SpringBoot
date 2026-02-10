package com.example.siapraja.controller;

import com.example.siapraja.dto.UserDTO;
import com.example.siapraja.dto.ResponData;
import com.example.siapraja.model.User;
import com.example.siapraja.security.MyUserDetails;
import com.example.siapraja.service.UserService;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    // CREATE
    @PostMapping
    public ResponseEntity<ResponData<User>> create(@Valid @RequestBody UserDTO userDTO, Errors errors) {
        ResponData<User> responseData = new ResponData<>();

        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessage().add(error.getDefaultMessage());
            }
            responseData.setStatus(false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }

        User user = modelMapper.map(userDTO, User.class);
        responseData.setStatus(true);
        responseData.setPayload(userService.save(user));
        responseData.getMessage().add("User created successfully");
        return ResponseEntity.ok(responseData);
    }

    // CREATE ALL

    // FIND ALL
    @GetMapping
    public ResponseEntity<ResponData<Iterable<User>>> findAll() {
        ResponData<Iterable<User>> responseData = new ResponData<>();
        responseData.setStatus(true);
        responseData.setPayload(userService.findAll());
        return ResponseEntity.ok(responseData);
    }

    // FIND BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponData<User>> findById(@PathVariable("id") Long id) {
        ResponData<User> responseData = new ResponData<>();
        User user = userService.findById(id);

        if (user == null) {
            responseData.setStatus(false);
            responseData.getMessage().add("User with ID " + id + " not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
        }

        responseData.setStatus(true);
        responseData.setPayload(user);
        return ResponseEntity.ok(responseData);
    }

    // EDIT / UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ResponData<User>> update(@PathVariable("id") Long id, @Valid @RequestBody UserDTO userDTO,
            Errors errors) {
        ResponData<User> responseData = new ResponData<>();

        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessage().add(error.getDefaultMessage());
            }
            responseData.setStatus(false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }

        try {
            User user = modelMapper.map(userDTO, User.class);
            responseData.setStatus(true);
            responseData.setPayload(userService.edit(id, user));
            responseData.getMessage().add("User updated successfully");
            return ResponseEntity.ok(responseData);
        } catch (RuntimeException e) {
            responseData.setStatus(false);
            responseData.getMessage().add(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile(@AuthenticationPrincipal MyUserDetails currentUser) {
        if (currentUser == null) {
            return ResponseEntity.status(401).body("Belum login");
        }

        Map<String, Object> details = new HashMap<>();
        details.put("username", currentUser.getUsername());
        details.put("authorities", currentUser.getAuthorities());
        details.put("userId", currentUser.getUserId());

        return ResponseEntity.ok(details);
    }
}
