package com.example.siapraja.controller;

import java.util.Collections;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import com.example.siapraja.dto.PresenceDTO;
import com.example.siapraja.dto.ResponData;
import com.example.siapraja.model.Presence;
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

    @PostMapping("/checkin/{studentId}")
    public ResponseEntity<ResponData<Presence>> checkIn(@PathVariable("studentId") Long studentId, @Valid @RequestBody PresenceDTO presenceDTO, Errors errors) {

        ResponData<Presence> responseData = new ResponData<>();

        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessage().add(error.getDefaultMessage());
            }
            responseData.setStatus(false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }

        try {
            Presence presence = modelMapper.map(presenceDTO, Presence.class);
            responseData.setStatus(true);
            responseData.setPayload(presenceService.checkIn(presence, studentId));
            responseData.setMessage(Collections.singletonList("Check-in successful"));
            return ResponseEntity.ok(responseData);
        } catch (RuntimeException e) {
            responseData.setStatus(false);
            responseData.setMessage(Collections.singletonList(e.getMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
    }

    @PutMapping("/checkout/{studentId}")
    public ResponseEntity<ResponData<Presence>> checkOut(@PathVariable("studentId") Long studentId) {
        ResponData<Presence> responseData = new ResponData<>();
        try {
            responseData.setStatus(true);
            responseData.setPayload(presenceService.checkOut(studentId));
            responseData.setMessage(Collections.singletonList("Check-out successful"));
            return ResponseEntity.ok(responseData);
        } catch (RuntimeException e) {
            responseData.setStatus(false);
            responseData.setMessage(Collections.singletonList(e.getMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
    }

    @GetMapping("/history/{monitoringId}")
    public ResponseEntity<ResponData<Iterable<Presence>>> getHistory(@PathVariable("monitoringId") Long monitoringId) {
        ResponData<Iterable<Presence>> responseData = new ResponData<>();
        responseData.setStatus(true);
        responseData.setPayload(presenceService.getHistoryByMonitoring(monitoringId));
        responseData.setMessage(Collections.singletonList("History retrieved successfully"));
        return ResponseEntity.ok(responseData);
    }
}