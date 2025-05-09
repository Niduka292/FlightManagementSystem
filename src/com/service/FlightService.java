package com.service;

import com.DTO.AirportDTO;
import com.DTO.CustomerDTO;
import com.DTO.FlightDTO;
import com.DTO.Seat;
import java.sql.*;
import com.util.JDBCUtil;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;


public class FlightService {
    
    public static boolean scheduleFlight(FlightDTO flight){
        
        boolean flightScheduleSuccess = false;
        Connection conn = null;
        PreparedStatement pst = null;
        
        String insertQuery = "INSERT INTO flights_table(flight_id,departure_utc_time"
                + ",departing_airport_id,destination_airport_id, transit_airports_id"
                + ",customers_list,seats_list,aircraft_id) VALUES (?,?,?,?,?,?,?,?)";
        
        try{
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(insertQuery);
            pst.setString(1, flight.getFlightID());
            pst.setString(2, JDBCUtil.convertZoneDateTimeToString(flight.getDepartureDate()));
            pst.setString(3, flight.getDepartingFrom().getAirportID());
            pst.setString(4, flight.getDestination().getAirportID());
            
            List<String> airportIds = new ArrayList<>();
            for (AirportDTO airport : flight.getTransitAirports()) {
                airportIds.add(airport.getAirportID());
            }
            Array transitAirportArr = conn.createArrayOf("text", airportIds.toArray());
            pst.setArray(5, transitAirportArr);

            
            Array customersArr = conn.createArrayOf("text", flight.getCustomers().toArray());
            pst.setArray(6, customersArr);
            
            Array seatsArr = conn.createArrayOf("text", flight.getSeats().toArray());
            pst.setArray(7, seatsArr);
            
            pst.setString(8, flight.getAircraft().getAircraftID());
            
            int rowsAffected = pst.executeUpdate();
            
            if(rowsAffected > 0){
                System.out.println("Flight scheduled successfully.");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(pst != null){
                    pst.close();
                }
                
                if(conn != null){
                    conn.close();
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return flightScheduleSuccess;
    }
    
    public static FlightDTO getFlightById(String flightId){
        
        FlightDTO flight = null;
        String selectQuery = "SELECT * FROM flights_table WHERE flight_id = ?";
        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement pst = null;
        
        try{
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(selectQuery);
            pst.setString(1, flightId);
            rs = pst.executeQuery();
            
            while(rs.next()){                
                flight = new FlightDTO();
                flight.setFlightID(rs.getString(1));
                
                ZonedDateTime departureZdt = JDBCUtil.convertStringToZonedDateTime(rs.getString(2));
                flight.setDepartureDate(departureZdt);
                
                AirportDTO departingAirport = BookingService.getAirportById(rs.getString(3));
                flight.setDepartingAirport(departingAirport);
                
                AirportDTO destinationAirport = BookingService.getAirportById(rs.getString(4));
                flight.setDestination(destinationAirport);
                
                Array transitAirportArray = rs.getArray("transit_airports_id"); // Get the SQL Array
                String[] transitAirportIds = (String[]) transitAirportArray.getArray(); // Convert it to a Java array
                
                List<AirportDTO> transitAirports = new ArrayList<>();
                for(String airportId : transitAirportIds){
                    transitAirports.add(BookingService.getAirportById(airportId));
                }
                flight.setTransitAirports(transitAirports);
                
                Array customersArray = rs.getArray("customers_list"); // Get the SQL Array
                String[] customerIds = (String[]) customersArray.getArray(); // Convert it to a Java array
                
                List<CustomerDTO> customers = new ArrayList<>();
                for(String customerid : customerIds){
                    customers.add(UserService.getCustomerById(customerid));
                }
                flight.setCustomers(customers);
                
                Array seatsArray = rs.getArray("seats_list"); // Get the SQL Array
                String[] seatIDs = (String[]) seatsArray.getArray(); // Convert it to a Java array
                
                List<Seat> seats = new ArrayList<>();
                for(String seatId : seatIDs){
                    Seat seat = SeatService.getSeatById(seatId);
                    seat.setFlight(flight);
                    seats.add(seat);
                }
                flight.setSeats(seats);
                
                flight.setAircraft(AircraftService.getAircraftById(rs.getString("aircraft_id")));
                
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(rs != null){
                    rs.close();
                }
                
                if(pst != null){
                    pst.close();
                }
                
                if(conn != null){
                    conn.close();
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return flight;
    }
        
    public static List<FlightDTO> getFlightAvailable(AirportDTO origin, AirportDTO destination,
            ZonedDateTime timeStart, ZonedDateTime timeEnd){
        
        List<FlightDTO> availableFlights = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        String selectQuery = "SELECT * FROM flights_table where departing_airport_id = ?";
        
        try{
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(selectQuery);
            pst.setString(1, origin.getAirportID());
            rs = pst.executeQuery();
            while(rs.next()){
                FlightDTO flight = new FlightDTO();
                
                flight.setFlightID(rs.getString("flight_id"));
                
                ZonedDateTime departureZdt = JDBCUtil.convertStringToZonedDateTime(rs.getString("departure_utc_time"));
                flight.setDepartureDate(departureZdt);
                
                flight.setDepartingAirport(BookingService.getAirportById(rs.getString("departing_airport_id")));
                flight.setDestination(BookingService.getAirportById(rs.getString("destination_airport_id")));
                
                Array transitAirportArray = rs.getArray("transit_airports_id"); // Get the SQL Array
                String[] transitAirportIds = (String[]) transitAirportArray.getArray(); // Convert it to a Java array
                
                List<AirportDTO> transitAirports = new ArrayList<>();
                for(String airportId : transitAirportIds){
                    transitAirports.add(BookingService.getAirportById(airportId));
                }
                flight.setTransitAirports(transitAirports);
                
                Array customersArray = rs.getArray("customers_list"); // Get the SQL Array
                String[] customerIds = (String[]) customersArray.getArray(); // Convert it to a Java array
                
                List<CustomerDTO> customers = new ArrayList<>();
                for(String customerid : customerIds){
                    customers.add(UserService.getCustomerById(customerid));
                }
                flight.setCustomers(customers);
                
                Array seatsArray = rs.getArray("seats_list"); // Get the SQL Array
                String[] seatIDs = (String[]) seatsArray.getArray(); // Convert it to a Java array
                
                List<Seat> seats = new ArrayList<>();
                for(String seatId : seatIDs){
                    seats.add(SeatService.getSeatById(seatId));
                }
                flight.setSeats(seats);
                
                flight.setAircraft(AircraftService.getAircraftById(rs.getString("aircraft_id")));
                
                if(transitAirports.contains(destination)){
                    if(departureZdt.isAfter(timeStart) && departureZdt.isBefore(timeEnd)){
                        availableFlights.add(flight);
                    }
                }
                
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(rs != null){
                    rs.close();
                }
                
                if(pst != null){
                    pst.close();
                }
                
                if(conn != null){
                    conn.close();
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return availableFlights;
        
        
    }
        
    
}
