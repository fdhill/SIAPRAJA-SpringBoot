package com.example.siapraja.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.siapraja.model.Company;
import com.example.siapraja.model.User;
import com.example.siapraja.repository.CompanyRepository;

@Service
@Transactional
public class CompanyService {
    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    UserService userService;

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public Company findById(Long id) {
        return companyRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Company with id " + id + " not found!"));
    }

    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.userId")
    @Transactional(readOnly = true)
    public Company findByUserId(Long id) {
        return companyRepository.findByUserId(id)
            .orElseThrow(() -> new RuntimeException("Company Profile not found"));
    }

    @Transactional(readOnly = true)
    public Iterable<Company> findAll() {
        return companyRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Company save(Company company) {
        User newUser = new User();
        newUser.setName(company.getName());
        newUser.setUsername(company.getName());
        newUser.setPassword("123456");
        newUser.setRole(3);

        User savedUser = userService.save(newUser);

        company.setUser(savedUser);

        return companyRepository.save(company);
    }

    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.userId")
    public Company edit(Long id, Company companyDetails) {
        Company existingCompany = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company with ID " + id + " not found"));

        existingCompany.setName(companyDetails.getName());
        existingCompany.setAddress(companyDetails.getAddress());
        existingCompany.setPhone(companyDetails.getPhone());
        existingCompany.setQuota(companyDetails.getQuota());

        return existingCompany;
    }
}
