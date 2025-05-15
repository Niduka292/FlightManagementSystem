package com.DTO;

public class AircraftDTO {
    
    private String aircraftID;
    private String model;
    private String category;
    private int noOfFirstClassSeats;
    private int noOfBusinessSeats;
    private int noOfEconomySeats;

    public static final int MIN_SEATS = 20;
    public static final int MIN_SEATS_ABOVE_SMALL = 100;
    public static final int MAX_SEATS_BELOW_LARGE = 250;
    
    public AircraftDTO() {
    }

    public String getAircraftID() {
        return aircraftID;
    }

    public void setAircraftID(String aircraftID) {
        this.aircraftID = aircraftID;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getNoOfFirstClassSeats() {
        return noOfFirstClassSeats;
    }

    public void setNoOfFirstClassSeats(int noOfFirstClassSeats) {
        if (noOfFirstClassSeats > 0) {
            this.noOfFirstClassSeats = noOfFirstClassSeats;
        }
    }

    public int getNoOfBusinessSeats() {
        return noOfBusinessSeats;
    }

    public void setNoOfBusinessSeats(int noOfBusinessSeats) {
        if (noOfBusinessSeats > 0) {
            this.noOfBusinessSeats = noOfBusinessSeats;
        }
    }

    public int getNoOfEconomySeats() {
        return noOfEconomySeats;
    }

    public void setNoOfEconomySeats(int noOfEconomySeats) {
        if (noOfEconomySeats > 0) {
            this.noOfEconomySeats = noOfEconomySeats;
        }
    }

    public AircraftDTO(String aircraftID, String model, int noOfFirstClassSeats, int noOfBusinessSeats, int noOfEconomySeats) {
        setAircraftID(aircraftID);
        setModel(model);
        setNoOfFirstClassSeats(noOfFirstClassSeats);
        setNoOfBusinessSeats(noOfBusinessSeats);
        setNoOfEconomySeats(noOfEconomySeats);
        setCategory(getCategoryByTotalSeats());
    }

    public String getCategoryByTotalSeats() {

        int totalSeats = noOfBusinessSeats + noOfEconomySeats + noOfFirstClassSeats;

        if (totalSeats >= MIN_SEATS) {
            if (totalSeats >= MIN_SEATS && totalSeats <= MIN_SEATS_ABOVE_SMALL) {
                return "small";
            } else if (totalSeats > MIN_SEATS_ABOVE_SMALL && totalSeats <= MAX_SEATS_BELOW_LARGE) {
                return "medium";
            } else {
                return "large";
            }
        }else{
            return "invalid";
        }
    }

}
