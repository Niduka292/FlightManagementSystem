package com.DTO;

public class Seat {
    
    private Long seatId;
    private FlightDTO flight;
    private boolean isBooked = false;
    private ServiceClass classOfService;
    private String seatNo;
    
    public Seat() {
    }

    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
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

    @Override
    public String toString() {
        return seatNo;
    }
    
    
}
