package com.DTO;

import java.time.ZonedDateTime;

public class BookingDTO {
    
    private long bookingID;
    private ZonedDateTime dateBooked; // Used this instead of Date because the timezone need to be specified 
    private CustomerDTO customer;
    private ServiceClass classOfService;
    private AirportDTO departingAirport;
    private AirportDTO destination;
    private FlightDTO flight;
    private Seat seat;
    private static int bookingIdIndex = 0;

    public BookingDTO() {
    }

    public long getBookingID() {
        return bookingID;
    }

    public void setBookingID(long bookingID) {
        this.bookingID = bookingID;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }    

    public ZonedDateTime getBookedDate() {
        return dateBooked;
    }

    public void setBookedDate(ZonedDateTime dateBooked) {
        this.dateBooked = dateBooked;
    }

    public ServiceClass getClassOfService() {
        return classOfService;
    }

    public void setClassOfService(ServiceClass classOfService) {
        this.classOfService = classOfService;
    }

    public AirportDTO getDepartingAirport() {
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

    public FlightDTO getFlight() {
        return flight;
    }

    public void setFlight(FlightDTO flight) {
        this.flight = flight;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public BookingDTO(CustomerDTO customer, 
            ServiceClass classOfService, AirportDTO departingAirport, AirportDTO destination, FlightDTO flight, Seat seat) {
        
        setBookedDate(ZonedDateTime.now());
        setCustomer(customer);
        setClassOfService(classOfService);
        setDepartingAirport(departingAirport);
        setDestination(destination);
        setFlight(flight);
        setSeat(seat);
        bookingIdIndex++;
    }
    
    
    
}
