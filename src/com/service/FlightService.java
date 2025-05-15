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
import java.time.ZoneOffset;
import java.util.Arrays;

public class FlightService {

    public static final int HOURS_OCCUPIED_BY_A_FLIGHT = 22;
    public static final int HOURS_SET_BEFORE_TARGET = 22;
    public static final int HOURS_SET_AFTER_TARGET = 15;
    
    public static boolean scheduleFlight(FlightDTO flight){
        
        boolean flightScheduleSuccess = false;
        Connection conn = null;
        PreparedStatement pst = null;
        
        if(!checkAircraftAvailability(flight.getAircraft().getAircraftID(), flight.getDepartureDate())){
            return false;
        }
        
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
                    if(strCustomerId != null && !strCustomerId.isEmpty() && !strCustomerId.equalsIgnoreCase("null")){
                        customerIds.add(Long.parseLong(strCustomerId.trim()));
                    }else{
                        System.out.println("Invalid customer Id");
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
        
    public static List<FlightDTO> getDirectFlightsAvailable(String originAirportCode, String destinationAirportCode,
            ZonedDateTime timeStart, ZonedDateTime timeEnd){
        
        List<FlightDTO> directFlights = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        String selectQuery = "SELECT * FROM flights_table where departing_airport_id = ?";
        
        try{
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(selectQuery);
            
            AirportDTO origin = AirportService.getAirportById(originAirportCode, conn);
            AirportDTO destination = AirportService.getAirportById(destinationAirportCode, conn);
            
            pst.setString(1, origin.getAirportCode());
            rs = pst.executeQuery();
            
            while(rs.next()){
                FlightDTO flight = new FlightDTO();
                
                long flightId = rs.getLong("flight_id");
                flight.setFlightID(flightId);
                
                ZonedDateTime departureZdt = JDBCUtil.convertStringToZonedDateTime(rs.getString("departure_utc_time"));
                flight.setDepartureDate(departureZdt);
                
                AirportDTO flightDestination = AirportService.getAirportById(rs.getString("destination_airport_id"),conn);
                
                flight.setDepartingAirport(AirportService.getAirportById(rs.getString("departing_airport_id"),conn));
                flight.setDestination(flightDestination);
                
                Array transitAirportArray = rs.getArray("transit_airports_id"); // Get the SQL Array
                String[] transitAirportIds = (String[]) transitAirportArray.getArray(); // Convert it to a Java array
                
                List<AirportDTO> transitAirports = new ArrayList<>();
                for(String airportId : transitAirportIds){
                    transitAirports.add(AirportService.getAirportById(airportId,conn));
                }
                flight.setTransitAirports(transitAirports);
                
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
                
                if(destination.equals(flightDestination)){
                    if(departureZdt.isAfter(timeStart) && departureZdt.isBefore(timeEnd)){
                        directFlights.add(flight);
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
        
        return directFlights;
    }
    
    
    public static List<FlightDTO> getTransitFlightsAvailable(String originAirportCode, String destinationAirportCode,
            ZonedDateTime timeStart, ZonedDateTime timeEnd){
        
        List<FlightDTO> transitFlights = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        String selectQuery = "SELECT * FROM flights_table where departing_airport_id = ?";
        
        try{
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(selectQuery);
            
            AirportDTO origin = AirportService.getAirportById(originAirportCode, conn);
            AirportDTO destination = AirportService.getAirportById(destinationAirportCode, conn);
            
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
                        transitFlights.add(flight);
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
        
        return transitFlights;
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
    
    public static List<ZonedDateTime> getDepartureTimesForAircraft(String aircraftId){
        
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<ZonedDateTime> departureTimes = new ArrayList<>();
        
        String selectQuery = "SELECT departure_utc_time FROM flights_table WHERE aircraft_id = ?";
        
        try{
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(selectQuery);
            pst.setString(1, aircraftId);
            rs = pst.executeQuery();
            while(rs.next()){
                
                String utcDepartureTime = rs.getString("departure_utc_time").trim();
                ZonedDateTime departureZdt = ZonedDateTime.parse(utcDepartureTime).withZoneSameInstant(ZoneOffset.UTC);
                departureTimes.add(departureZdt);
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
        
        return departureTimes;
    }
    
    public static ZonedDateTime getLatestDepartureForAircraft(String aircraftId){
        
        List<ZonedDateTime> zdtDepartures = getDepartureTimesForAircraft(aircraftId);
        
        if(zdtDepartures.isEmpty()){
            return null;
        }
        
        ZonedDateTime latestDeparture = zdtDepartures.get(0);
        
        if(zdtDepartures.size() > 1){
            for(int i = 1; i < zdtDepartures.size();i++){
                if(zdtDepartures.get(i).isAfter(latestDeparture)){
                    latestDeparture = zdtDepartures.get(i);
                }
            }
        }
        
        return latestDeparture;
    }
    
    public static boolean checkAircraftAvailability(String aircrftId, ZonedDateTime flightScheduledTime){
        
        boolean isAvailableForFlying = false;
        
        ZonedDateTime normalizeScheduledTime = flightScheduledTime.withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime latestDepartureForAircraft = getLatestDepartureForAircraft(aircrftId);
        
        if(latestDepartureForAircraft == null){
            return true;
        }
        
        if(normalizeScheduledTime.isAfter(latestDepartureForAircraft.plusHours(HOURS_OCCUPIED_BY_A_FLIGHT))){
            isAvailableForFlying = true;
        }
        
        return isAvailableForFlying;
        
    }
    
    public static List<FlightDTO> getArrivingFlightsForAirport(String airportCode, ZonedDateTime targetZdtTime){
        
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<FlightDTO> arrivingFlights = new ArrayList<>();
        
        String selectQuery = "SELECT flight_id,departure_utc_time,departing_airport_id,destination_airport_id, transit_airports_id,seats_list,aircraft_id FROM flights_table";
        
        try{
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(selectQuery);
            rs = pst.executeQuery();
            
            while(rs.next()){
                
                FlightDTO flight = new FlightDTO();
                
                long flightId = rs.getLong("flight_id");
                flight.setFlightID(flightId);
                
                ZonedDateTime departureZdt = JDBCUtil.convertStringToZonedDateTime(rs.getString("departure_utc_time"));
                flight.setDepartureDate(departureZdt);
                
                flight.setDepartingAirport(AirportService.getAirportById(rs.getString("departing_airport_id"),conn));
                flight.setDestination(AirportService.getAirportById(rs.getString("destination_airport_id"),conn));
                
                String destination = rs.getString("destination_airport_id");
                
                Array transitAirportArray = rs.getArray("transit_airports_id"); // Get the SQL Array
                String[] transitAirportIdsArr = (String[]) transitAirportArray.getArray(); // Convert it to a Java array
                List<String> transitAirportIds = Arrays.asList(transitAirportIdsArr);
                
                List<AirportDTO> transitAirports = new ArrayList<>();
                for(String airportId : transitAirportIds){
                    transitAirports.add(AirportService.getAirportById(airportId,conn));
                }
                flight.setTransitAirports(transitAirports);
                
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
                
                boolean isArrivingAtAirport = airportCode.trim().equals(destination.trim()) || 
                               transitAirportIds.contains(airportCode.trim());

                ZonedDateTime normalizedDepartureZdt = departureZdt.withZoneSameInstant(ZoneOffset.UTC);
                ZonedDateTime normalizedTargetZdt = targetZdtTime.withZoneSameInstant(ZoneOffset.UTC);
                
                boolean isWithinTimeWindow = normalizedDepartureZdt.isAfter(normalizedTargetZdt.minusHours(HOURS_SET_BEFORE_TARGET)) &&
                             normalizedDepartureZdt.isBefore(normalizedTargetZdt.plusHours(HOURS_SET_AFTER_TARGET));

                
                if(isArrivingAtAirport && isWithinTimeWindow){
                    arrivingFlights.add(flight);
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
        
        return arrivingFlights;
    }
    
    public static List<FlightDTO> getDepartingFlightsForAirport(String airportCode, ZonedDateTime targetZdtTime){
        
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<FlightDTO> departingFlights = new ArrayList<>();
        
        String selectQuery = "SELECT flight_id,departure_utc_time,departing_airport_id,destination_airport_id, transit_airports_id,seats_list,aircraft_id FROM flights_table";
        
        try{
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(selectQuery);
            rs = pst.executeQuery();
            
            while(rs.next()){
                
                FlightDTO flight = new FlightDTO();
                
                long flightId = rs.getLong("flight_id");
                flight.setFlightID(flightId);
                
                ZonedDateTime departureZdt = JDBCUtil.convertStringToZonedDateTime(rs.getString("departure_utc_time"));
                flight.setDepartureDate(departureZdt);
                
                String origin = rs.getString("departing_airport_id");
                String destination = rs.getString("destination_airport_id");
                
                flight.setDepartingAirport(AirportService.getAirportById(origin,conn));
                flight.setDestination(AirportService.getAirportById(destination,conn));
                
                Array transitAirportArray = rs.getArray("transit_airports_id"); // Get the SQL Array
                String[] transitAirportIdsArr = (String[]) transitAirportArray.getArray(); // Convert it to a Java array
                List<String> transitAirportIds = Arrays.asList(transitAirportIdsArr);
                
                List<AirportDTO> transitAirports = new ArrayList<>();
                for(String airportId : transitAirportIds){
                    transitAirports.add(AirportService.getAirportById(airportId,conn));
                }
                flight.setTransitAirports(transitAirports);
                
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
                
                boolean isDepartingFromAirport = airportCode.trim().equals(origin.trim());

                ZonedDateTime normalizedDepartureZdt = departureZdt.withZoneSameInstant(ZoneOffset.UTC);
                ZonedDateTime normalizedTargetZdt = targetZdtTime.withZoneSameInstant(ZoneOffset.UTC);
                
                boolean isWithinTimeWindow = normalizedDepartureZdt.isBefore(normalizedTargetZdt.plusHours(HOURS_SET_AFTER_TARGET)) 
                        && normalizedDepartureZdt.isAfter(normalizedTargetZdt);
                
                if(isDepartingFromAirport && isWithinTimeWindow){
                    departingFlights.add(flight);
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
        
        return departingFlights;
    }
    
}
