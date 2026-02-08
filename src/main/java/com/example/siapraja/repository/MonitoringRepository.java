package com.example.siapraja.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.siapraja.model.Monitoring;

public interface MonitoringRepository extends JpaRepository<Monitoring, Long>{
    
}
