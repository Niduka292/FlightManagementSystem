package com.gui;

import java.time.ZonedDateTime;

public class Flight2 {
   
    private Long flightId;
    private String originAirportCode;
    private String destinationAirportCode;
    private ZonedDateTime departureDate;

    public Flight2(Long flightId, String originAirportCode, String destinationAirportCode, ZonedDateTime departureDate) {
        this.flightId = flightId;
        this.originAirportCode = originAirportCode;
        this.destinationAirportCode = destinationAirportCode;
        this.departureDate = departureDate;
    }

    public Long getFlightId() {
        return flightId;
    }

    public String getOriginAirportCode() {
        return originAirportCode;
    }

    public String getDestinationAirportCode() {
        return destinationAirportCode;
    }

    public ZonedDateTime getDepartureDate() {
        return departureDate;
    }
    
    
}
