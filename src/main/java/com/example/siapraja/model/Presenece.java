package com.example.siapraja.model;
import java.time.LocalDate;
import java.time.LocalTime;

public class Presenece {
    private Long id;
    private Monitoring monitoring;
    private String location;
    private String notes;
    private int status;
    private LocalDate date;
    private LocalTime chekin_time;
    private LocalTime chekout_time;

    public Presenece(Monitoring monitoring, String location, String notes, int status, LocalDate date,
            LocalTime chekin_time) {
        this.monitoring = monitoring;
        this.location = location;
        this.notes = notes;
        this.status = status;
        this.date = date;
        this.chekin_time = chekin_time;
    }

    public Presenece() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Monitoring getMonitoring() {
        return monitoring;
    }

    public void setMonitoring(Monitoring monitoring) {
        this.monitoring = monitoring;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getChekin_time() {
        return chekin_time;
    }

    public void setChekin_time(LocalTime chekin_time) {
        this.chekin_time = chekin_time;
    }

    public LocalTime getChekout_time() {
        return chekout_time;
    }

    public void setChekout_time(LocalTime chekout_time) {
        this.chekout_time = chekout_time;
    }

}
