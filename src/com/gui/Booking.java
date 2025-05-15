package com.gui;

import java.time.LocalDate;

public class Booking {
    
    private LocalDate bookedDate;
    private String classOfService;
    private String departedAirportCode;
    private String destinationAirportCode;
    private String seatNo;

    public Booking(LocalDate bookedDate, String classOfService, String departedAirportCode, String destinationAirportCode, String seatNo) {
        this.bookedDate = bookedDate;
        this.classOfService = classOfService;
        this.departedAirportCode = departedAirportCode;
        this.destinationAirportCode = destinationAirportCode;
        this.seatNo = seatNo;
    }

    public LocalDate getBookedDate() {
        return bookedDate;
    }

    public String getClassOfService() {
        return classOfService;
    }

    public String getDepartedAirportCode() {
        return departedAirportCode;
    }

    public String getDestinationAirportCode() {
        return destinationAirportCode;
    }

    public String getSeatNo() {
        return seatNo;
    }
    
    
}
