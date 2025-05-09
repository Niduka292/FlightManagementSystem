package com.DTO;

public class AircraftDTO {

    private String aircraftID;
    private String model;
    private String category;
    private int noOfFirstClassSeats;
    private int noOfBusinessSeats;
    private int noOfEconomySeats;

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

        if (totalSeats >= 20) {
            if (totalSeats >= 20 && totalSeats <= 100) {
                return "small";
            } else if (totalSeats > 100 && totalSeats <= 250) {
                return "medium";
            } else {
                return "large";
            }
        }else{
            return "invalid";
        }
    }

}
