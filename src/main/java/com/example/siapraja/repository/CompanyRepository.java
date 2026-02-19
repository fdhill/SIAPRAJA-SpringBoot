package com.example.siapraja.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.siapraja.model.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    public Optional<Company> findByUserId(Long userId);
}
