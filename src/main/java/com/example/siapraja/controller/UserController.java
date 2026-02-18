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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<ResponData<Iterable<User>>> findAll() {
        ResponData<Iterable<User>> responseData = new ResponData<>();

        responseData.setStatus(true);
        responseData.setPayload(userService.findAll());
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponData<User>> findById(@PathVariable("id") Long id) {
        ResponData<User> responseData = new ResponData<>();

        responseData.setStatus(true);
        responseData.setPayload(userService.findById(id));
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/myaccount")
    public ResponseEntity<?> findByUserIdLogin(@AuthenticationPrincipal MyUserDetails currentUser) {
        if (currentUser == null) {
            return ResponseEntity.status(401).body("Belum login");
        }

        Map<String, Object> details = new HashMap<>();
        details.put("username", currentUser.getUsername());
        details.put("authorities", currentUser.getAuthorities());
        details.put("userId", currentUser.getUserId());

        return ResponseEntity.ok(details);
    }
    
    @PostMapping
    public ResponseEntity<ResponData<User>> create(@Valid @RequestBody UserDTO userDTO, Errors errors) {
        ResponData<User> responseData = new ResponData<>();

        User user = modelMapper.map(userDTO, User.class);
        responseData.setStatus(true);
        responseData.setPayload(userService.save(user));
        responseData.getMessage().add("User created successfully");
        return ResponseEntity.ok(responseData);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponData<User>> update(@PathVariable("id") Long id, @Valid @RequestBody UserDTO userDTO, Errors errors) {
        ResponData<User> responseData = new ResponData<>();

        User user = modelMapper.map(userDTO, User.class);
        responseData.setStatus(true);
        responseData.setPayload(userService.edit(id, user));
        responseData.getMessage().add("User updated successfully");
        return ResponseEntity.ok(responseData);
    }

}
