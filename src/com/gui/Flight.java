package com.gui;

import java.time.ZonedDateTime;

public class Flight {
    
    private Long flightId;
    private String departingCode;
    private String destinationCode;
    private ZonedDateTime departureTime;
    private String status;

    public Flight(Long flightId, String departingCode, String destinationCode, ZonedDateTime departureTime,String status) {
        this.flightId = flightId;
        this.departingCode = departingCode;
        this.destinationCode = destinationCode;
        this.departureTime = departureTime;
        this.status = status;
    }

    public Long getFlightId() {
        return flightId;
    }

    public String getDepartingCode() {
        return departingCode;
    }

    public String getDestinationCode() {
        return destinationCode;
    }

    public ZonedDateTime getDepartureTime() {
        return departureTime;
    }

    public String getStatus() {
        return status;
    }
    
    
    
    
}
