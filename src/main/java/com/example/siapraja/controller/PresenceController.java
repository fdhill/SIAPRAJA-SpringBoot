package com.example.siapraja.controller;

import java.util.Collections;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import com.example.siapraja.dto.PresenceDTO;
import com.example.siapraja.dto.ResponData;
import com.example.siapraja.model.Presence;
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


    @GetMapping("/history/{monitoringId}")
    public ResponseEntity<ResponData<Iterable<Presence>>> getHistory(@PathVariable("monitoringId") Long monitoringId) {
        ResponData<Iterable<Presence>> responseData = new ResponData<>();
        responseData.setStatus(true);
        responseData.setPayload(presenceService.getHistoryByMonitoring(monitoringId));
        responseData.setMessage(Collections.singletonList("History retrieved successfully"));
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/mypresences")
    public ResponseEntity<ResponData<Iterable<Presence>>> getMyPresenceHistory(@AuthenticationPrincipal MyUserDetails currentUser) {
        ResponData<Iterable<Presence>> responseData = new ResponData<>();

        responseData.setStatus(true);
        responseData.setPayload(presenceService.findMyPresenceHistory(currentUser.getUserId()));
        responseData.setMessage(Collections.singletonList("History retrieved successfully"));
        return ResponseEntity.ok(responseData);
    }

    // @PostMapping("/checkin")
    // public ResponseEntity<ResponData<Presence>> checkIn(@Valid @RequestBody PresenceDTO presenceDTO, @AuthenticationPrincipal MyUserDetails currentUser, Errors errors) {
    //     ResponData<Presence> responseData = new ResponData<>();

    //     Presence presence = modelMapper.map(presenceDTO, Presence.class);
    //     responseData.setStatus(true);
    //     responseData.setPayload(presenceService.checkIn(presence, currentUser.getUserId()));
    //     responseData.setMessage(Collections.singletonList("Check-in successful"));
    //     return ResponseEntity.ok(responseData);
    // }

    @PutMapping("/checkout")
    public ResponseEntity<ResponData<Presence>> checkOut(@AuthenticationPrincipal MyUserDetails currentUser) {
        ResponData<Presence> responseData = new ResponData<>();

        responseData.setStatus(true);
        responseData.setPayload(presenceService.checkOut(currentUser.getUserId()));
        responseData.setMessage(Collections.singletonList("Check-out successful"));
        return ResponseEntity.ok(responseData);
    }
}