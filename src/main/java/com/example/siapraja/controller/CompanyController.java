package com.example.siapraja.controller;

import com.example.siapraja.dto.CompanyDTO;
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
    public ResponseEntity<ResponData<Iterable<Company>>> findAll() {
        ResponData<Iterable<Company>> responseData = new ResponData<>();

        responseData.setPayload(companyService.findAll());
        responseData.setStatus(true);
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponData<Company>> findById(@PathVariable("id") Long id) {
        ResponData<Company> responseData = new ResponData<>();

        responseData.setPayload(companyService.findById(id));
        responseData.setStatus(true);
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/myprofile")
    public ResponseEntity<ResponData<Company>> findByUserIdLogin(Authentication authentication) {
        ResponData<Company> responseData = new ResponData<>();

        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();

        responseData.setPayload(companyService.findByUserId(userDetails.getUserId()));
        responseData.setStatus(true);
        return ResponseEntity.ok(responseData);
    }

    @PostMapping
    public ResponseEntity<ResponData<Company>> create(@Valid @RequestBody CompanyDTO companyDTO) {
        ResponData<Company> responseData = new ResponData<>();

        Company company = modelMapper.map(companyDTO, Company.class);
        responseData.setStatus(true);
        responseData.setPayload(companyService.save(company));
        responseData.getMessage().add("Company created successfully");
        return ResponseEntity.ok(responseData);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponData<Company>> update(@PathVariable("id") Long id,
            @Valid @RequestBody CompanyDTO companyDTO) {
        ResponData<Company> responseData = new ResponData<>();

        Company company = modelMapper.map(companyDTO, Company.class);
        responseData.setPayload(companyService.edit(id, company));
        responseData.setStatus(true);
        responseData.getMessage().add("Company updated successfully");
        return ResponseEntity.ok(responseData);
    }

    @PreAuthorize("hasRole('COMPANY')")
    @PutMapping("/update-profile")
    public ResponseEntity<ResponData<Company>> updateMyProfile(
            @AuthenticationPrincipal MyUserDetails currentUser,
            @Valid @RequestBody CompanyDTO companyDTO) {

        ResponData<Company> responseData = new ResponData<>();

        Company myCompany = currentUser.getProfileAs(Company.class);

        if (myCompany == null) {
            throw new RuntimeException("Profil company not found for this user");
        }

        Company companyData = modelMapper.map(companyDTO, Company.class);

        responseData.setPayload(companyService.edit(myCompany.getId(), companyData));
        responseData.setStatus(true);
        responseData.getMessage().add("Profile updated successfully");

        return ResponseEntity.ok(responseData);
    }
}