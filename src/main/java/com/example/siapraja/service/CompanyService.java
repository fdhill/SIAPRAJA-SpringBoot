package com.example.siapraja.service;

import org.springframework.beans.factory.annotation.Autowired;
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
    public Company findById(Long id) {
        return companyRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Iterable<Company> findAll() {
        return companyRepository.findAll();
    }

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

    public Iterable<Company> saveAll(Iterable<Company> company) {
        return companyRepository.saveAll(company);
    }

    public Company edit(Long id, Company companyDetails) {

        Company existingCompany = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        existingCompany.setName(companyDetails.getName());
        existingCompany.setAddress(companyDetails.getAddress());
        existingCompany.setPhone(companyDetails.getPhone());
        existingCompany.setQuota(companyDetails.getQuota());

        User user = existingCompany.getUser();
        user.setName(companyDetails.getName());
        userService.edit(user.getId(), user);

        return existingCompany;
    }
}
