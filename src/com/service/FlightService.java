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
        
        String insertQuery = "INSERT INTO flights_table(departure_utc_time"
                + ",departing_airport_id,destination_airport_id, transit_airports_id"
                + ",customers_list,seats_list,aircraft_id) VALUES (?,?,?,?,?,?,?)";
        
        try{
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(insertQuery);
            pst.setString(1, JDBCUtil.convertZoneDateTimeToString(flight.getDepartureDate()));
            pst.setString(2, flight.getDepartingFrom().getAirportCode());
            pst.setString(3, flight.getDestination().getAirportCode());
            
            List<String> airportCodes = new ArrayList<>();
            for (AirportDTO airport : flight.getTransitAirports()) {
                airportCodes.add(airport.getAirportCode());
            }
            Array transitAirportArr = conn.createArrayOf("text", airportCodes.toArray());
            pst.setArray(4, transitAirportArr);
            
            List<CustomerDTO> customers = flight.getCustomers();
            String[] customerIds = new String[customers.size()];
            
            for(int i = 0; i < customers.size(); i++){
                
                Long id = customers.get(i).getUserID();
                customerIds[i] = String.valueOf(id);
            }
            
            Array customersArr = conn.createArrayOf("text", customerIds);
            pst.setArray(5, customersArr);
            
            
            pst.setArray(6, null);
            
            pst.setString(7, flight.getAircraft().getAircraftID());
            
            int rowsAffected = pst.executeUpdate();
            
            if(rowsAffected > 0){
                System.out.println("Flight scheduled successfully.");
                flightScheduleSuccess = true;
            }
            
            long flightId = getFlightIdByDetails(flight);
            flight.setFlightID(flightId);
            
            List<Seat> seatsList = SeatService.createSeatList(flight, flight.getAircraft());
            flight.setSeats(seatsList);
            
            List<Seat> seats = flight.getSeats();
            String[] seatIds = new String[seats.size()];
            
            for(int i = 0; i < seats.size(); i++){
                
                Long id = seats.get(i).getSeatId();
                seatIds[i] = String.valueOf(id);
            }
            
            Array seatsArr = conn.createArrayOf("text",seatIds);
            
            String updateQuery = "UPDATE flights_table SET seats_list = ? WHERE flight_id = ?";
            
            PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
            updateStmt.setArray(1, seatsArr);
            updateStmt.setLong(2, flight.getFlightID());
            updateStmt.executeUpdate();
            updateStmt.close();
            
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
    
    public static FlightDTO getFlightById(long flightId){
        
        FlightDTO flight = null;
        String selectQuery = "SELECT * FROM flights_table WHERE flight_id = ?";
        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement pst = null;
        
        try{
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(selectQuery);
            pst.setLong(1, flightId);
            rs = pst.executeQuery();
            
            if(rs.next()){                
                flight = new FlightDTO();
                flight.setFlightID(rs.getLong(1));
                
                ZonedDateTime departureZdt = JDBCUtil.convertStringToZonedDateTime(rs.getString(2));
                flight.setDepartureDate(departureZdt);
                
                AirportDTO departingAirport = AirportService.getAirportById(rs.getString(3),conn);
                flight.setDepartingAirport(departingAirport);
                
                AirportDTO destinationAirport = AirportService.getAirportById(rs.getString(4),conn);
                flight.setDestination(destinationAirport);
                
                Array transitAirportArray = rs.getArray("transit_airports_id"); // Get the SQL Array
                String[] transitAirportIds = (String[]) transitAirportArray.getArray(); // Convert it to a Java array
                
                List<AirportDTO> transitAirports = new ArrayList<>();
                for(String airportId : transitAirportIds){
                    transitAirports.add(AirportService.getAirportById(airportId,conn));
                }
                flight.setTransitAirports(transitAirports);
                
                Array customersArray = rs.getArray("customers_list"); // Get the SQL Array
                String[] stringCustomerIds = (String[]) customersArray.getArray(); // Convert it to a Java array
                
                List<Long> customerIds = new ArrayList<>();
                for(String strCustomerId : stringCustomerIds){
                    if(strCustomerId != null && !strCustomerId.isEmpty()){
                        customerIds.add(Long.parseLong(strCustomerId));
                    }
                }
                
                List<CustomerDTO> customers = new ArrayList<>();
                for(long customerid : customerIds){
                    customers.add(UserService.getCustomerById(customerid,conn));
                }
                flight.setCustomers(customers);
                
                Array seatsArray = rs.getArray("seats_list"); // Get the SQL Array
                String[] seatIDs = (String[]) seatsArray.getArray(); // Convert it to a Java array
                
                List<Seat> seats = new ArrayList<>();
                for(String strSeatId : seatIDs){
                    
                    if(strSeatId != null && !strSeatId.isEmpty()){
                        long seatId = Long.parseLong(strSeatId);
                        Seat seat = SeatService.getSeatById(seatId,conn);
                        seat.setFlight(flight);
                        seats.add(seat);
                    }
                    
                    
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
            pst.setString(1, origin.getAirportCode());
            rs = pst.executeQuery();
            
            while(rs.next()){
                FlightDTO flight = new FlightDTO();
                
                long flightId = rs.getLong("flight_id");
                flight.setFlightID(flightId);
                
                ZonedDateTime departureZdt = JDBCUtil.convertStringToZonedDateTime(rs.getString("departure_utc_time"));
                flight.setDepartureDate(departureZdt);
                
                flight.setDepartingAirport(AirportService.getAirportById(rs.getString("departing_airport_id"),conn));
                flight.setDestination(AirportService.getAirportById(rs.getString("destination_airport_id"),conn));
                
                Array transitAirportArray = rs.getArray("transit_airports_id"); // Get the SQL Array
                String[] transitAirportIds = (String[]) transitAirportArray.getArray(); // Convert it to a Java array
                
                List<AirportDTO> transitAirports = new ArrayList<>();
                for(String airportId : transitAirportIds){
                    transitAirports.add(AirportService.getAirportById(airportId,conn));
                }
                flight.setTransitAirports(transitAirports);
                
                Array customersArray = rs.getArray("customers_list"); // Get the SQL Array
                String[] stringCustomerIds = (String[]) customersArray.getArray(); // Convert it to a Java array

                List<Long> customerIds = new ArrayList<>();
                for(String strCustomerId : stringCustomerIds){
                    if(strCustomerId != null && !strCustomerId.isEmpty()){
                        customerIds.add(Long.parseLong(strCustomerId));
                    }
                }
                
                List<CustomerDTO> customers = new ArrayList<>();
                for(long customerid : customerIds){
                    customers.add(UserService.getCustomerById(customerid,conn));
                }
                flight.setCustomers(customers);
                
                Array seatsArray = rs.getArray("seats_list"); // Get the SQL Array
                String[] seatIDs = (String[]) seatsArray.getArray(); // Convert it to a Java array
                List<Seat> seats = new ArrayList<>();
                for(String strSeatId : seatIDs){
                    
                    if(strSeatId != null && !strSeatId.isEmpty()){
                        long seatId = Long.parseLong(strSeatId);
                        Seat seat = SeatService.getSeatById(seatId,conn);
                        seat.setFlight(flight);
                        seats.add(seat);
                    }
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
        
    public static FlightDTO getFlightBasicById(long flightId, Connection conn) {
        
        FlightDTO flight = new FlightDTO();
        PreparedStatement pst = null;
        ResultSet rs = null;

        String query = "SELECT flight_id, departure_utc_time, departing_airport_id, destination_airport_id, aircraft_id FROM flights_table WHERE flight_id = ?";

        try {
            pst = conn.prepareStatement(query);
            pst.setLong(1, flightId);
            rs = pst.executeQuery();

            if (rs.next()) {
                
                AirportDTO departingAirport = AirportService.getAirportById(rs.getString("departing_airport_id"),conn);
                AirportDTO destinationAirport = AirportService.getAirportById(rs.getString("destination_airport_id"),conn);
                
                flight.setFlightID(rs.getLong("flight_id"));
                flight.setDepartureDate(JDBCUtil.convertStringToZonedDateTime(rs.getString("departure_utc_time")));
                flight.setDepartingAirport(departingAirport);
                flight.setDestination(destinationAirport);
                flight.setAircraft(AircraftService.getAircraftById(rs.getString("aircraft_id")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return flight;
    }
    
    public static Long getFlightIdByDetails(FlightDTO flight){
        
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        Long flightId = null;
        
        String selectQuery = "SELECT flight_id FROM flights_table WHERE departure_utc_time = ? AND departing_airport_id = ?"
                + " AND destination_airport_id = ?";
        
        try{
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(selectQuery);
            pst.setString(1, JDBCUtil.convertZoneDateTimeToString(flight.getDepartureDate()));
            pst.setString(2, flight.getDepartingFrom().getAirportCode());
            pst.setString(3, flight.getDestination().getAirportCode());
            rs = pst.executeQuery();
            
            if(rs.next()){
                
                flightId = rs.getLong("flight_id");
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
        
        return flightId;
    }    

    public static void addCustomerToFlight(CustomerDTO customer, FlightDTO flight, Connection conn){
        
        PreparedStatement pst = null;
        List<CustomerDTO> existingCustomers = UserService.getCustomersByFlight(flight.getFlightID(), conn);
        
        existingCustomers.add(customer);
        
        String updateQuery = "UPDATE flights_table SET customers_list = ? WHERE flight_id = ?";
        
        String[] customerIds = new String[existingCustomers.size()];

        for (int i = 0; i < existingCustomers.size(); i++) {

            Long id = existingCustomers.get(i).getUserID();
            customerIds[i] = String.valueOf(id);
        }
        
        try{
            pst = conn.prepareStatement(updateQuery);
            
            Array customersArr = conn.createArrayOf("text", customerIds);
            pst.setArray(1, customersArr);
            pst.setLong(2, flight.getFlightID());
            
            int rowsUpdated = pst.executeUpdate();
            
            if(rowsUpdated > 0){
                System.out.println("Customer "+customer.getUserID()+" added to the flight "+flight.getFlightID()+" successfully.");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(pst != null){
                    pst.close();
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
    }
    
}
