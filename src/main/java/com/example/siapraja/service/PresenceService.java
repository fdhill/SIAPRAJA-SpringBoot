package com.example.siapraja.service;

import com.example.siapraja.dto.PresenceResponDTO;
import com.example.siapraja.model.Monitoring;
import com.example.siapraja.model.Presence;
import com.example.siapraja.repository.MonitoringRepository;
import com.example.siapraja.repository.PresenceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class PresenceService {

    private final ModelMapper modelMapper;

    @Autowired
    private PresenceRepository presenceRepository;

    @Autowired
    private MonitoringService monitoringService;

    @Autowired
    private MonitoringRepository monitoringRepository;

    PresenceService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    public Iterable<PresenceResponDTO> findAll() {
        Iterable<Presence> presences = presenceRepository.findAll();

        return StreamSupport.stream(presences.spliterator(), false)
            .map(presence -> modelMapper.map(presence, PresenceResponDTO.class))
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Iterable<PresenceResponDTO> getHistoryByMonitoringId(Long monitoringId) {
        Iterable<Presence> presences = presenceRepository.findByMonitoringIdOrderByDateDesc(monitoringId);

        return StreamSupport.stream(presences.spliterator(), false)
            .map(presence -> modelMapper.map(presence, PresenceResponDTO.class))
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @PreAuthorize("#studentId == authentication.principal.studentId")
    public Iterable<PresenceResponDTO> findMyPresenceHistoryByStudentId(Long studentId) {
        Iterable<Presence> presences = presenceRepository.findByMonitoring_StudentId(studentId);

        return StreamSupport.stream(presences.spliterator(), false)
            .map(presence -> modelMapper.map(presence, PresenceResponDTO.class))
            .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('STUDENT') and #studentId == authentication.principal.studentId")
    public PresenceResponDTO checkIn(Long studentId, Presence presence) {
        LocalDate today = LocalDate.now();

        Monitoring monitoring = monitoringRepository.findByStudentId(studentId)
            .orElseThrow(() -> new RuntimeException("Monitoring with id " + studentId + "not found"));

        if (presenceRepository.findByMonitoringIdAndDate(monitoring.getId(), today).isPresent()) {
            throw new RuntimeException("You have already checked in today!");
        }

        presence.setMonitoring(monitoring);

        presence.setDate(today);
        presence.setCheckinTime(LocalTime.now());

        monitoringService.setMonitoringStartDate(monitoring.getId(), today);

        return modelMapper.map(presenceRepository.save(presence), PresenceResponDTO.class);
    }

    @PreAuthorize("hasRole('STUDENT')")
    public PresenceResponDTO checkOut(Long presenceId) {

        Presence presence = presenceRepository.findById(presenceId)
            .orElseThrow(() -> new RuntimeException("Presence not found!"));

        if(presence.getCheckoutTime() != null){
            new RuntimeException("you have already checked");
        }
        presence.setCheckoutTime(LocalTime.now());

        return modelMapper.map(presenceRepository.save(presence), PresenceResponDTO.class);
    }
}