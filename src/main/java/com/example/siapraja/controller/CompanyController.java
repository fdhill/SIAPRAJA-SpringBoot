package com.example.siapraja.controller;

import com.example.siapraja.dto.CompanyDTO;
import com.example.siapraja.dto.ResponData;
import com.example.siapraja.model.Company;
import com.example.siapraja.service.CompanyService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ModelMapper modelMapper;

    // CREATE
    @PostMapping
    public ResponseEntity<ResponData<Company>> create(@Valid @RequestBody CompanyDTO companyDTO, Errors errors) {
        ResponData<Company> responseData = new ResponData<>();

        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessage().add(error.getDefaultMessage());
            }
            responseData.setStatus(false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }

        Company company = modelMapper.map(companyDTO, Company.class);
        responseData.setStatus(true);
        responseData.setPayload(companyService.save(company));
        responseData.getMessage().add("Company created successfully");
        return ResponseEntity.ok(responseData);
    }

    // FIND ALL
    @GetMapping
    public ResponseEntity<ResponData<Iterable<Company>>> findAll() {
        ResponData<Iterable<Company>> responseData = new ResponData<>();
        responseData.setStatus(true);
        responseData.setPayload(companyService.findAll());
        return ResponseEntity.ok(responseData);
    }

    // FIND BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponData<Company>> findById(@PathVariable("id") Long id) {
        ResponData<Company> responseData = new ResponData<>();
        Company company = companyService.findById(id);
        
        if (company == null) {
            responseData.setStatus(false);
            responseData.getMessage().add("Company with ID " + id + " not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
        }

        responseData.setStatus(true);
        responseData.setPayload(company);
        return ResponseEntity.ok(responseData);
    }

    // EDIT / UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ResponData<Company>> update(@PathVariable("id") Long id, @Valid @RequestBody CompanyDTO companyDTO, Errors errors) {
        ResponData<Company> responseData = new ResponData<>();

        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessage().add(error.getDefaultMessage());
            }
            responseData.setStatus(false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }

        try {
            Company company = modelMapper.map(companyDTO, Company.class);
            responseData.setStatus(true);
            responseData.setPayload(companyService.edit(id, company));
            responseData.getMessage().add("Company updated successfully");
            return ResponseEntity.ok(responseData);
        } catch (RuntimeException e) {
            responseData.setStatus(false);
            responseData.getMessage().add(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
        }
    }
}