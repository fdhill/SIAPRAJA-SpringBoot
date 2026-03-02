package com.example.siapraja.controller;

import com.example.siapraja.dto.CompanyRequestDTO;
import com.example.siapraja.dto.CompanyResponDTO;
import com.example.siapraja.dto.ResponData;
import com.example.siapraja.model.Company;
import com.example.siapraja.security.MyUserDetails;
import com.example.siapraja.service.CompanyService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<ResponData<Iterable<CompanyResponDTO>>> findAll() {
        ResponData<Iterable<CompanyResponDTO>> responseData = new ResponData<>();

        responseData.setPayload(companyService.findAll());
        responseData.setStatus(true);
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponData<CompanyResponDTO>> findById(@PathVariable("id") Long id) {
        ResponData<CompanyResponDTO> responseData = new ResponData<>();

        responseData.setPayload(companyService.findById(id));
        responseData.setStatus(true);
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/myprofile")
    public ResponseEntity<ResponData<CompanyResponDTO>> getMyProfile(Authentication authentication) {
        ResponData<CompanyResponDTO> responseData = new ResponData<>();

        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();

        responseData.setPayload(companyService.findByUserId(userDetails.getUserId()));
        responseData.setStatus(true);
        return ResponseEntity.ok(responseData);
    }

    @PostMapping
    public ResponseEntity<ResponData<CompanyResponDTO>> create(
            @Valid @RequestBody CompanyRequestDTO companyRequestDTO) {
        ResponData<CompanyResponDTO> responseData = new ResponData<>();

        responseData.setStatus(true);
        responseData.setPayload(companyService.save(companyRequestDTO));
        responseData.getMessage().add("Company created successfully");
        return ResponseEntity.ok(responseData);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponData<CompanyResponDTO>> update(@PathVariable("id") Long id,
            @Valid @RequestBody CompanyRequestDTO companyRequestDTO) {
        ResponData<CompanyResponDTO> responseData = new ResponData<>();

        responseData.setPayload(companyService.edit(id, companyRequestDTO));
        responseData.setStatus(true);
        responseData.getMessage().add("Company updated successfully");
        return ResponseEntity.ok(responseData);
    }

    @PreAuthorize("hasRole('COMPANY')")
    @PutMapping("/update-profile")
    public ResponseEntity<ResponData<CompanyResponDTO>> updateMyProfile(
            @AuthenticationPrincipal MyUserDetails currentUser,
            @Valid @RequestBody CompanyRequestDTO companyRequestDTO) {

        ResponData<CompanyResponDTO> responseData = new ResponData<>();

        Long companyId = currentUser.getCompanyId();

        if (companyId == null) {
            throw new RuntimeException("Profile company not found for this user");
        }

        responseData.setPayload(companyService.edit(companyId, companyRequestDTO));
        responseData.setStatus(true);
        responseData.getMessage().add("Profile updated successfully");

        return ResponseEntity.ok(responseData);
    }
}