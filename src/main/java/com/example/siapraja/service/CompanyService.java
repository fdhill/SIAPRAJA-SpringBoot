package com.example.siapraja.service;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.siapraja.dto.CompanyRequestDTO;
import com.example.siapraja.dto.CompanyResponDTO;
import com.example.siapraja.dto.UserRequestDTO;
import com.example.siapraja.model.Company;
import com.example.siapraja.model.User;
import com.example.siapraja.repository.CompanyRepository;

@Service
@Transactional
public class CompanyService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    UserService userService;

    @Transactional(readOnly = true)
    public CompanyResponDTO findById(Long id) {
        return companyRepository.findById(id)
                .map(company -> modelMapper.map(company, CompanyResponDTO.class))
                .orElseThrow(() -> new RuntimeException("Company with id " + id + " not found!"));
    }

    @PreAuthorize("hasRole('COMPANY') and #id == authentication.principal.userId")
    @Transactional(readOnly = true)
    public CompanyResponDTO findByUserId(Long id) {
        return companyRepository.findByUserId(id)
                .map(company -> modelMapper.map(company, CompanyResponDTO.class))
                .orElseThrow(() -> new RuntimeException("Company with user id " + id + " not found!"));
    }

    @Transactional(readOnly = true)
    public Iterable<CompanyResponDTO> findAll() {
        Iterable<Company> companies = companyRepository.findAll();

        return StreamSupport.stream(companies.spliterator(), false)
                .map(company -> modelMapper.map(company, CompanyResponDTO.class))
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN')")
    public CompanyResponDTO save(CompanyRequestDTO companyRequestDTO) {
        
        UserRequestDTO newUser = new UserRequestDTO();
        newUser.setName(companyRequestDTO.getName());
        newUser.setUsername(companyRequestDTO.getName());
        newUser.setPassword("123456");
        newUser.setRole(3);
        User savedUser = modelMapper.map(userService.save(newUser), User.class);
        
        Company company = modelMapper.map(companyRequestDTO, Company.class);
        company.setUser(savedUser);

        return modelMapper.map(companyRepository.save(company), CompanyResponDTO.class);
    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('COMPANY') and #companyId == authentication.principal.companyId)")
    public CompanyResponDTO edit(Long companyId, CompanyRequestDTO companyRequestDTO) {
        Company existingCompany = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company with ID " + companyId + " not found"));

        existingCompany.setName(companyRequestDTO.getName());
        existingCompany.setAddress(companyRequestDTO.getAddress());
        existingCompany.setPhone(companyRequestDTO.getPhone());
        existingCompany.setQuota(companyRequestDTO.getQuota());

        return modelMapper.map(companyRepository.save(existingCompany), CompanyResponDTO.class);
    }
}
