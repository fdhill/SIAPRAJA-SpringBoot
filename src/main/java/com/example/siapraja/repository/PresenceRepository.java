package com.example.siapraja.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.siapraja.model.Presence;

public interface PresenceRepository extends JpaRepository<Presence, Long>{
    Iterable<Presence> findByMonitoringId(Long monitoringId);

    Iterable<Presence> findByMonitoringIdOrderByDateDesc(Long monitoringId);

    Optional<Presence> findByMonitoringIdAndDate(Long monitoringId, LocalDate date);

    Iterable<Presence> findByMonitoringIdAndStatus(Long monitoringId, int status);
}
