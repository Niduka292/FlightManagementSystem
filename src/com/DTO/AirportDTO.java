package com.DTO;

public class AirportDTO {
    
    private Long airportID;
    private String airportCode;
    private String airportName;
    private String city;
    private String country;
    private String continent;

    public AirportDTO() {
    }

    public Long getAirportID() {
        return airportID;
    }

    public void setAirportID(Long airportID) {
        this.airportID = airportID;
    }

    public String getAirportCode() {
        return airportCode;
    }

    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }

    public String getAirportName() {
        return airportName;
    }

    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public AirportDTO(String airportName, String city, String country, String continent) {
        
        if(airportName != null && airportName.trim().length() >= 4){
            airportCode = airportName.toUpperCase().trim().substring(0, 4); 
            // Auto generate airportID according to the airport name
        }else if(airportName != null){
            airportCode = airportName.toUpperCase().trim();
        }else{
            airportCode = "UNKN";
        }
        
        setAirportID(airportID);
        setAirportName(airportName);
        setCity(city);
        setCountry(country);
        setContinent(continent);
    }

    @Override
    public String toString() {
        String msg = "Airport ID: "+airportID;
        msg += "\nName: "+airportName;
        msg += "\nCountry: "+country;
        
        return msg;
    }
    
    
    
}
