package com.example.siapraja.controller;

import com.example.siapraja.dto.ResponData;
import com.example.siapraja.dto.LoginRequest;
import com.example.siapraja.security.MyUserDetails;
import com.example.siapraja.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<ResponData<Map<String, String>>> authenticateUser(@RequestBody LoginRequest loginRequest) {
        ResponData<Map<String, String>> responseData = new ResponData<>();

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtUtils.generateJwtToken(authentication);

            MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            Map<String, String> payload = new HashMap<>();
            payload.put("token", jwt);
            payload.put("type", "Bearer");
            payload.put("roles", roles.toString());

            responseData.setStatus(true);
            responseData.setPayload(payload);
            responseData.getMessage().add("Login successful");

            return ResponseEntity.ok(responseData);

        } catch (AuthenticationException e) {
            responseData.setStatus(false);
            responseData.getMessage().add("Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseData);
        }
    }
}