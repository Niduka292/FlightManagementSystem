package com.service;

import com.DTO.AircraftDTO;
import com.DTO.FlightDTO;
import com.DTO.Seat;
import com.DTO.ServiceClass;
import java.sql.*;
import com.util.JDBCUtil;
import java.util.ArrayList;
import java.util.List;

public class SeatService {
    
    public static void addSeat(Seat seat){
        
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet generatedKeys = null;
        
        String insertQuery = "INSERT INTO seats_table(is_booked, class_of_service, flight_id,seat_no) VALUES(?,?,?,?)";
        
        try{
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(insertQuery,Statement.RETURN_GENERATED_KEYS); 
            
            pst.setBoolean(1, seat.getIsBooked());
            pst.setString(2, seat.getClassOfService().toString());
            pst.setLong(3, FlightService.getFlightIdByDetails(seat.getFlight()));
            pst.setString(4, seat.getSeatNo());
            
            int rowsAffected = pst.executeUpdate();
            
            if(rowsAffected > 0){
                generatedKeys = pst.getGeneratedKeys();
                if(generatedKeys.next()){
                    Long generataedId = generatedKeys.getLong(1);
                    seat.setSeatId(generataedId);
                }
                System.out.println("Seat "+seat.getSeatNo()+"successfully added.");
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
        
    }
    
    public static boolean checkIsSeatBooked(String seatNo, long flightId){
        
        Connection conn = null;
        PreparedStatement pst = null;
        boolean isBooked = false;
        ResultSet rs =  null;
        
        String selectQuery = "SELECT is_booked FROM seats_table WHERE seat_no = ? AND flight_id = ?";
        
        try{
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(selectQuery);
            pst.setString(1, seatNo);
            pst.setLong(2, flightId);
            rs = pst.executeQuery();
            
            if(rs.next()){
                isBooked = rs.getBoolean("is_booked");
                if(isBooked){
                    System.out.println("Seat "+seatNo+"is booked");
                }
            }else{
                System.out.println("Seat "+seatNo+" was mot founf");
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
        
        return isBooked;
    }
    
    public static boolean bookSeat(String seatNo, long flightId) {
        if (seatNo == null || seatNo.isEmpty()) {
            System.out.println("Seat number is invalid.");
            return false;
        }

        Seat seat = getSeatBySeatNo(seatNo, flightId);
        if (seat == null) {
            System.out.println("Seat " + seatNo + " not found for flight " + flightId);
            return false;
        }

        if (checkIsSeatBooked(seatNo, flightId)) {
            System.out.println("Seat " + seatNo + " is already booked.");
            return false;
        }

        return updateSeatStatus(seatNo, flightId, true);
    }

    
    private static boolean updateSeatStatus(String seatNo, long flightId, boolean isBooked) {
        String updateQuery = "UPDATE seats_table SET is_booked = ? WHERE seat_no = ? AND flight_id = ?";

        try {
            Connection conn = JDBCUtil.getConnection();
            PreparedStatement pst = conn.prepareStatement(updateQuery);
            pst.setBoolean(1, isBooked);
            pst.setString(2, seatNo);
            pst.setLong(3, flightId);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static Seat getSeatBySeatNo(String seatNo, long flightId){
        
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs =  null;
        Seat seat = new Seat();
        
        String selectQuery = "SELECT * FROM seats_table where seat_no = ? AND flight_id = ?";
        
        try{
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(selectQuery);
            pst.setString(1, seatNo);
            pst.setLong(2, flightId);
            rs = pst.executeQuery();
            
            if(rs.next()){
                seat.setSeatId(rs.getLong("seat_id"));
                seat.setIsBooked(rs.getBoolean("is_booked"));
                seat.setClassOfService(ServiceClass.valueOf(rs.getString("class_of_service").trim().toUpperCase()));
                seat.setFlight(FlightService.getFlightBasicById(rs.getLong("flight_id"),conn));
                seat.setSeatNo(rs.getString("seat_no"));
                
            }else{
                System.out.println("Seat "+seatNo+" was mot found.");
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
        
        return seat;
    }
    
    public static Seat getSeatById(long seatId, Connection conn){
        
        PreparedStatement pst = null;
        ResultSet rs =  null;
        Seat seat = new Seat();
        
        String selectQuery = "SELECT * FROM seats_table where seat_id = ?";
        
        try{
            pst = conn.prepareStatement(selectQuery);
            pst.setLong(1, seatId);
            rs = pst.executeQuery();
            
            if(rs.next()){
                seat.setSeatId(rs.getLong("seat_id"));
                seat.setIsBooked(rs.getBoolean("is_booked"));
                seat.setClassOfService(ServiceClass.valueOf(rs.getString("class_of_service").trim().toUpperCase()));
                seat.setFlight(FlightService.getFlightBasicById(rs.getLong("flight_id"),conn));
                seat.setSeatNo(rs.getString("seat_no"));
                
            }else{
                System.out.println("Seat "+seatId+" was mot found.");
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
                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return seat;
    }
    
    public static List<Seat> getSeatsByFlightId(long flightId){
        
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<Seat> seats = new ArrayList<>();
        
        String selectQuery = "SELECT FROM seats_table WHERE flight_id = ?";
        
        try{
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(selectQuery);
            pst.setLong(1, flightId);
            rs = pst.executeQuery();
            
            while(rs.next()){
                Seat seat = new Seat();
                seat.setSeatNo(rs.getString("seat_id"));
                seat.setClassOfService(ServiceClass.valueOf(rs.getString("class_of_service")));
                seat.setFlight(FlightService.getFlightBasicById(rs.getLong("flight_id"),conn));
                seat.setIsBooked(rs.getBoolean("is_booked"));
                seats.add(seat);
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
        
        return seats;
        
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
            
}
