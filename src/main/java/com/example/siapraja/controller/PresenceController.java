package com.example.siapraja.controller;

import java.util.Collections;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.example.siapraja.dto.PresenceRequestDTO;
import com.example.siapraja.dto.PresenceResponDTO;
import com.example.siapraja.dto.ResponData;
import com.example.siapraja.security.MyUserDetails;
import com.example.siapraja.service.PresenceService;
import com.example.siapraja.service.MonitoringService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/presences")
public class PresenceController {

    @Autowired
    private PresenceService presenceService;

    @Autowired
    private MonitoringService monitoringService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<ResponData<Iterable<PresenceResponDTO>>> findAll() {
        ResponData<Iterable<PresenceResponDTO>> responseData = new ResponData<>();

        responseData.setStatus(true);
        responseData.setPayload(presenceService.findAll());
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/history/{monitoringId}")
    public ResponseEntity<ResponData<Iterable<PresenceResponDTO>>> getHistory(@PathVariable("monitoringId") Long monitoringId) {
        ResponData<Iterable<PresenceResponDTO>> responseData = new ResponData<>();
        responseData.setStatus(true);
        responseData.setPayload(presenceService.getHistoryByMonitoringId(monitoringId));
        responseData.setMessage(Collections.singletonList("History retrieved successfully"));
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/mypresences")
    public ResponseEntity<ResponData<Iterable<PresenceResponDTO>>> getMyPresenceHistory(@AuthenticationPrincipal MyUserDetails currentUser) {
        ResponData<Iterable<PresenceResponDTO>> responseData = new ResponData<>();

        responseData.setStatus(true);
        responseData.setPayload(presenceService.findMyPresenceHistoryByStudentId(currentUser.getStudentId()));
        responseData.setMessage(Collections.singletonList("History retrieved successfully"));
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/checkin")
    public ResponseEntity<ResponData<PresenceResponDTO>> checkIn(@Valid @RequestBody PresenceRequestDTO presenceRequestDTO, @AuthenticationPrincipal MyUserDetails currentUser) {
        ResponData<PresenceResponDTO> responseData = new ResponData<>();

        responseData.setStatus(true);
        responseData.setPayload(presenceService.checkIn(currentUser.getStudentId(), presenceRequestDTO));
        responseData.setMessage(Collections.singletonList("Check-in successful"));
        return ResponseEntity.ok(responseData);
    }

    @PutMapping("/checkout/{presenceId}")
    public ResponseEntity<ResponData<PresenceResponDTO>> checkOut(@PathVariable("presenceId") Long presenceId) {
        ResponData<PresenceResponDTO> responseData = new ResponData<>();

        responseData.setStatus(true);
        responseData.setPayload(presenceService.checkOut(presenceId));
        responseData.setMessage(Collections.singletonList("Check-out successful"));
        return ResponseEntity.ok(responseData);
    }
}