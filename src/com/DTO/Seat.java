package com.DTO;

public class Seat {
    
    private String seatNo;
    private boolean isBooked = false;
    private ServiceClass classOfService;

    public Seat() {
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public boolean isIsBooked() {
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

    public Seat(String seatNo, ServiceClass classOfService) {
        setSeatNo(seatNo);
        setClassOfService(classOfService);
    }
    
    public void bookSeat(){
        setIsBooked(true);
    }
    
}
