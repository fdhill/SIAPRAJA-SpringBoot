package com.example.siapraja.controller;

import com.example.siapraja.dto.UserDTO;
import com.example.siapraja.dto.ResponData;
import com.example.siapraja.model.User;
import com.example.siapraja.security.MyUserDetails;
import com.example.siapraja.service.UserService;
import jakarta.validation.Valid;

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
    public ResponseEntity<ResponData<User>> getMyAccount(@AuthenticationPrincipal MyUserDetails currentUser){
        ResponData<User> responData = new ResponData<>();

        responData.setPayload(userService.findById(currentUser.getUserId()));
        responData.setStatus(true);
        return ResponseEntity.ok(responData);
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

    @PutMapping("/update-account")
    public ResponseEntity<ResponData<User>> updateMyaccount(
            @AuthenticationPrincipal MyUserDetails currentUser,
            @Valid @RequestBody UserDTO userDTO) {

        ResponData<User> responseData = new ResponData<>();

        User userData = modelMapper.map(userDTO, User.class);

        responseData.setPayload(userService.edit(currentUser.getUserId(), userData));
        responseData.setStatus(true);
        responseData.getMessage().add("Profile updated successfully");

        return ResponseEntity.ok(responseData);
    }
}
