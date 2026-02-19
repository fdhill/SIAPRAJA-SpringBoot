package com.example.siapraja.service;

import com.example.siapraja.model.Monitoring;
import com.example.siapraja.model.Presence;
import com.example.siapraja.repository.PresenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@Transactional
public class PresenceService {

    @Autowired
    private PresenceRepository presenceRepository;

    @Autowired
    private MonitoringService monitoringService;

    @Transactional(readOnly = true)
    public Iterable<Presence> findAll() {
        return presenceRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Iterable<Presence> getHistoryByMonitoringId(Long monitoringId) {
        return presenceRepository.findByMonitoringIdOrderByDateDesc(monitoringId);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("#studentId == authentication.principal.studentId")
    public Iterable<Presence> findMyPresenceHistoryByStudentId(Long studentId) {
        return presenceRepository.findByMonitoring_StudentId(studentId);
    }

    @PreAuthorize("hasRole('STUDENT') and #studentId == authentication.principal.studentId")
    public Presence checkIn(Long studentId, Presence presence) {
        LocalDate today = LocalDate.now();

        Monitoring monitoring = monitoringService.findByStudentId(studentId);

        if (presenceRepository.findByMonitoringIdAndDate(monitoring.getId(), today).isPresent()) {
            throw new RuntimeException("You have already checked in today!");
        }

        presence.setMonitoring(monitoring);

        presence.setDate(today);
        presence.setCheckinTime(LocalTime.now());

        monitoringService.setMonitoringStartDate(monitoring.getId(), today);

        return presenceRepository.save(presence);
    }

    // @PreAuthorize("hasRole('STUDENT') and #studentId == authentication.principal.studentId")
    public Presence checkOut(Long presenceId) {

        Presence presence = presenceRepository.findById(presenceId)
            .orElseThrow(() -> new RuntimeException("Presence not found!"));

        if(presence.getCheckoutTime() != null){
            new RuntimeException("you have already checked");
        }
        presence.setCheckoutTime(LocalTime.now());

        return presence;
    }
}