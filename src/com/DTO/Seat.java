package com.DTO;

import java.util.ArrayList;
import java.util.List;
import com.service.SeatService;

public class Seat {
    
    private long seatId;
    private FlightDTO flight;
    private boolean isBooked = false;
    private ServiceClass classOfService;
    private String seatNo;
    
    public Seat() {
    }

    public long getSeatId() {
        return seatId;
    }

    public void setSeatId(long seatId) {
        this.seatId = seatId;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public FlightDTO getFlight() {
        return flight;
    }

    public void setFlight(FlightDTO flight) {
        this.flight = flight;
    }
    
    public boolean getIsBooked() {
        return isBooked;
    }

    public void setIsBooked(boolean isBooked) {
        this.isBooked = isBooked;
    }

    public ServiceClass getClassOfService() {
        return classOfService;
    }

    public void setClassOfService(ServiceClass classOfService) {
        this.classOfService = classOfService;
    }

    public Seat(String seatNo, FlightDTO flight,ServiceClass classOfService) {
        setSeatNo(seatNo);
        setFlight(flight);
        setClassOfService(classOfService);
    }
    
    public void bookSeat(){
        setIsBooked(true);
    }
    
    public static List<Seat> createSeatList(FlightDTO flight, AircraftDTO aircraft){
        
        List<Seat> seatList = new ArrayList<>();

        if (aircraft != null) { // Null check for safety
            for (int i = 1; i <= aircraft.getNoOfFirstClassSeats(); i++) {
                
                String seatCode = String.format("F-%03d", i); // F-001
                Seat seat = new Seat(seatCode, flight,ServiceClass.FIRST);
                seatList.add(seat);
                SeatService.addSeat(seat);
           }

            for (int i = 1; i <= aircraft.getNoOfBusinessSeats(); i++) {
                
                String seatCode = String.format("B-%03d", i);
                Seat seat = new Seat(seatCode, flight,ServiceClass.BUSINESS);
                seatList.add(seat);
                SeatService.addSeat(seat);
            }

            for (int i = 1; i <= aircraft.getNoOfEconomySeats(); i++) {
                
                String seatCode = String.format("E-%03d", i);
                Seat seat = new Seat(seatCode, flight,ServiceClass.ECONOMY);
                seatList.add(seat);
                SeatService.addSeat(seat);
            }
        }
        
        return seatList;
    }

    @Override
    public String toString() {
        return seatNo;
    }
    
    
}
