package com.example.siapraja.controller;

import com.example.siapraja.dto.UserRequestDTO;
import com.example.siapraja.dto.UserResponDTO;
import com.example.siapraja.dto.ResponData;
import com.example.siapraja.security.MyUserDetails;
import com.example.siapraja.service.UserService;
import jakarta.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<ResponData<Iterable<UserResponDTO>>> findAll() {
        ResponData<Iterable<UserResponDTO>> responseData = new ResponData<>();

        responseData.setStatus(true);
        responseData.setPayload(userService.findAll());
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponData<UserResponDTO>> findById(@PathVariable("id") Long id) {
        ResponData<UserResponDTO> responseData = new ResponData<>();

        responseData.setStatus(true);
        responseData.setPayload(userService.findById(id));
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/myaccount")
    public ResponseEntity<ResponData<UserResponDTO>> getMyAccount(@AuthenticationPrincipal MyUserDetails currentUser){
        ResponData<UserResponDTO> responData = new ResponData<>();

        responData.setPayload(userService.findById(currentUser.getUserId()));
        responData.setStatus(true);
        return ResponseEntity.ok(responData);
    }
    
    @PostMapping
    public ResponseEntity<ResponData<UserResponDTO>> create(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        ResponData<UserResponDTO> responseData = new ResponData<>();

        responseData.setStatus(true);
        responseData.setPayload(userService.save(userRequestDTO));
        responseData.getMessage().add("User created successfully");
        return ResponseEntity.ok(responseData);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponData<UserResponDTO>> update(@PathVariable("id") Long id, @Valid @RequestBody UserRequestDTO userRequestDTO) {
        ResponData<UserResponDTO> responseData = new ResponData<>();

        responseData.setStatus(true);
        responseData.setPayload(userService.edit(id, userRequestDTO));
        responseData.getMessage().add("User updated successfully");
        return ResponseEntity.ok(responseData);
    }

    @PutMapping("/update-account")
    public ResponseEntity<ResponData<UserResponDTO>> updateMyaccount(@AuthenticationPrincipal MyUserDetails currentUser, @Valid @RequestBody UserRequestDTO userRequestDTO) {

        ResponData<UserResponDTO> responseData = new ResponData<>();

        responseData.setPayload(userService.edit(currentUser.getUserId(), userRequestDTO));
        responseData.setStatus(true);
        responseData.getMessage().add("Profile updated successfully");

        return ResponseEntity.ok(responseData);
    }
}
