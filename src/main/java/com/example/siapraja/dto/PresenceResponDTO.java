package com.example.siapraja.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class PresenceResponDTO {

    private Long id;

    private String location;

    private String notes;

    private int status;

    private LocalDate date;

    private LocalTime checkinTime;

    private LocalTime checkoutTime;
}
