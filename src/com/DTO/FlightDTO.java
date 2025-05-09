package com.DTO;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FlightDTO {

    private String flightID;
    private ZonedDateTime departureDate;
    private AirportDTO departingAirport;
    private AirportDTO destination;
    private AircraftDTO aircraft;
    private List<AirportDTO> transitAirports;
    private List<CustomerDTO> customers;
    private List<Seat> seats;
    private static int flightIdIndex = 0;
    
    public String getFlightID() {
        return flightID;
    }

    public void setFlightID(String flightID) {
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
        
        String index;
        if(flightIdIndex < 10){
            index = "00"+flightIdIndex;
        }else if(flightIdIndex < 100){
            index = "0"+flightIdIndex;
        }else{
            index = String.valueOf(flightIdIndex);
        }
        
        flightID = departingAirport.getAirportID()+index; // Auto generate flightID consisting 7 characters

        setFlightID(flightID);
        setDepartureDate(departureDate);
        setDepartingAirport(departingAirport);
        setDestination(destination);
        setAircraft(aircraft);
        setTransitAirports(transitAirports);
        setCustomers(customers);

        List<Seat> seatList = new ArrayList<>();

        if (aircraft != null) { // Null check for safety
            for (int i = 1; i <= aircraft.getNoOfFirstClassSeats(); i++) {
                
                String zeroPaddingFirstClass;
                if(i < 10){
                    zeroPaddingFirstClass = "F-00";
                }else if(i < 100){
                    zeroPaddingFirstClass = "F-0";
                }else{
                    zeroPaddingFirstClass = "F-";
                }
                
                Seat seat = new Seat(zeroPaddingFirstClass + i, ServiceClass.FIRST);
                seatList.add(seat);
            }

            for (int i = 1; i <= aircraft.getNoOfBusinessSeats(); i++) {
                
                String zeroPaddingBusinessClass;
                if(i < 10){
                    zeroPaddingBusinessClass = "B-00";
                }else if(i < 100){
                    zeroPaddingBusinessClass = "B-0";
                }else{
                    zeroPaddingBusinessClass = "B-";
                }
                
                Seat seat = new Seat(zeroPaddingBusinessClass+ i, ServiceClass.BUSINESS);
                seatList.add(seat);
            }

            for (int i = 1; i <= aircraft.getNoOfEconomySeats(); i++) {
                
                String zeroPaddingEconomyClass;
                if(i < 10){
                    zeroPaddingEconomyClass = "E-000";
                }else if(i < 100){
                    zeroPaddingEconomyClass = "E-00";
                }else if(i < 1000){
                    zeroPaddingEconomyClass = "E-0";
                }else{
                    zeroPaddingEconomyClass = "E-";
                }
                Seat seat = new Seat(flightID+"-"+zeroPaddingEconomyClass + i, ServiceClass.ECONOMY);
                seatList.add(seat);
            }
        }

        setSeats(seatList);

    }

}
