package com.DTO;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FlightDTO {

    private long flightID;
    private ZonedDateTime departureDate;
    private AirportDTO departingAirport;
    private AirportDTO destination;
    private AircraftDTO aircraft;
    private List<AirportDTO> transitAirports = new ArrayList<>();
    private List<CustomerDTO> customers = new ArrayList<>();
    private List<Seat> seats = new ArrayList<>();
    private static int flightIdIndex = 1;

    public FlightDTO() {
    }
    
    public long getFlightID() {
        return flightID;
    }

    public void setFlightID(long flightID) {
        this.flightID = flightID;        
    }

    public ZonedDateTime getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(ZonedDateTime departureDate) {
        this.departureDate = departureDate;
    }

    public AirportDTO getDepartingFrom() {
        return departingAirport;
    }

    public void setDepartingAirport(AirportDTO departingAirport) {
        this.departingAirport = departingAirport;
    }

    public AirportDTO getDestination() {
        return destination;
    }

    public void setDestination(AirportDTO destination) {
        this.destination = destination;
    }

    public AircraftDTO getAircraft() {
        return aircraft;
    }

    public void setAircraft(AircraftDTO aircraft) {
        this.aircraft = aircraft;
    }

    public List<AirportDTO> getTransitAirports() {
        return transitAirports;
    }

    public void setTransitAirports(List<AirportDTO> transitAirports) {
        this.transitAirports = transitAirports;
    }

    public List<CustomerDTO> getCustomers() {
        return customers;
    }

    public void setCustomers(List<CustomerDTO> customers) {
        this.customers = customers;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public FlightDTO(ZonedDateTime departureDate, AirportDTO departingAirport, 
            AirportDTO destination, AircraftDTO aircraft, List<AirportDTO> transitAirports, List<CustomerDTO> customers) {
        
        
        setDepartureDate(departureDate);
        setDepartingAirport(departingAirport);
        setDestination(destination);
        setAircraft(aircraft);
        setTransitAirports(transitAirports);
        setCustomers(customers);
        flightIdIndex++;

        
        List<Seat> seatList = Seat.createSeatList(this, aircraft);
        setSeats(seatList);

    }

}
