package com.example.siapraja.service;

import com.example.siapraja.model.Monitoring;
import com.example.siapraja.model.Presence;
import com.example.siapraja.repository.PresenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Presence checkIn(Presence presence, Long idUser) {
        LocalDate today = LocalDate.now();

        Monitoring monitoring = monitoringService.findStudentMonitoringByIdUser(idUser);

        if (presenceRepository.findByMonitoringIdAndDate(monitoring.getId(), today).isPresent()) {
            throw new RuntimeException("You have already checked in today!");
        }

        presence.setMonitoring(monitoring);

        presence.setDate(today);
        presence.setCheckinTime(LocalTime.now());

        monitoringService.setMonitoringStartDate(monitoring.getId(), today);

        return presenceRepository.save(presence);
    }

    public Presence checkOut(Long idUser) {
        LocalDate today = LocalDate.now();

        Monitoring monitoring = monitoringService.findByStudentId(idUser);

        Presence presence = presenceRepository.findByMonitoringIdAndDate(monitoring.getId(), today)
                .orElseThrow(() -> new RuntimeException("Anda belum melakukan check-in hari ini!"));

        presence.setCheckoutTime(LocalTime.now());

        return presence;
    }

    @Transactional(readOnly = true)
    public Iterable<Presence> getHistoryByMonitoring(Long monitoringId) {
        return presenceRepository.findByMonitoringIdOrderByDateDesc(monitoringId);
    }

    @Transactional(readOnly = true)
    public Iterable<Presence> findMyPresenceHistory(Long userId) {
        return presenceRepository.findByMonitoring_Student_User_Id(userId);
    }
}