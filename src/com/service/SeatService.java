package com.service;

import com.DTO.AirportDTO;
import com.DTO.BookingDTO;
import com.DTO.CustomerDTO;
import com.DTO.FlightDTO;
import com.DTO.Seat;
import com.DTO.ServiceClass;
import java.sql.*;
import com.util.JDBCUtil;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

public class SeatService {
    
    public static void addSeat(Seat seat){
        
        Connection conn = null;
        PreparedStatement pst = null;
        
        String insertQuery = "INSERT INTO seats_table(is_booked, class_of_service, flight_id,seat_no) VALUES(?,?,?,?)";
        
        try{
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(insertQuery);
            
            pst.setBoolean(1, seat.getIsBooked());
            pst.setString(2, seat.getClassOfService().toString());
            pst.setLong(3, seat.getFlight().getFlightID());
            pst.setString(4, seat.getSeatNo());
            
            int rowsAffected = pst.executeUpdate();
            
            if(rowsAffected > 0){
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
    
    
    public static boolean bookSeat(String seatNo, CustomerDTO customer, long flightId,
            AirportDTO origin, AirportDTO destination){
        
        if(seatNo == null || customer == null || origin == null || destination == null){
            System.out.println("Invalid parameters.");
            return false;
        }
        
        Seat seat = getSeatBySeatNo(seatNo,flightId);
        if(seat == null){
            System.out.println("Seat "+seatNo+" not found for flight "+flightId);
            return false;
        }
        
        boolean isPrevBooked = checkIsSeatBooked(seatNo,flightId);
        if(isPrevBooked){
            System.out.println("Seat "+seatNo+" is already booked");
            return false;
        }
        
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean isNowBooked = false;
        
        ZonedDateTime zdtNow = ZonedDateTime.now();
        FlightDTO flight = FlightService.getFlightById(flightId);
        
        if(flight == null){
            System.out.println("Flight "+flightId+" was not found");
            return false;
        }
        
        if(!flight.getDepartureDate().minusMinutes(30).isAfter(zdtNow)){
            System.out.println("Cannot book seat less than 30 minutes before departure");
            return false;
        }
        
        BookingDTO booking = new BookingDTO();
        booking.setCustomer(customer);
        booking.setSeat(seat);
        booking.setFlight(flight);
        booking.setClassOfService(seat.getClassOfService());
        booking.setBookedDate(zdtNow);
        booking.setDepartingAirport(origin);
        booking.setDestination(destination);
        
        if(!updateSeatStatus(seatNo, flightId, true)){
            return false;
        }
        
        String selectQuery = "SELECT is_booked FROM seats_table where seat_no = ? AND flight_id = ?";
        
        if(!isPrevBooked){
            if(seat.getFlight().getDepartureDate().minusMinutes(30).isAfter(zdtNow)){
                try{
                    conn = JDBCUtil.getConnection();
                    pst = conn.prepareStatement(selectQuery);
                    pst.setString(1, seatNo);
                    pst.setLong(2, flightId);
                    rs = pst.executeQuery();
                    
                    if(rs.next() && !rs.getBoolean("is_booked") && seat != null && flight != null){
                        isNowBooked = true;
                        //booking = new BookingDTO();
                        
                        
                    }else if(isPrevBooked){
                        System.out.println("Seat is already booked.");
                    }
                    
                    BookingService.createBooking(booking);
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
            }
        }else{
            isNowBooked = true;
        }
        
        return isNowBooked;
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
                seat.setSeatNo(rs.getString("seat_id"));
                seat.setIsBooked(rs.getBoolean("is_booked"));
                seat.setClassOfService(ServiceClass.valueOf(rs.getString("class_of_service").trim().toUpperCase()));
                seat.setFlight(FlightService.getFlightBasicById(rs.getLong("flight_id")));
                
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
    
    public static Seat getSeatById(long seatId){
        
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs =  null;
        Seat seat = new Seat();
        
        String selectQuery = "SELECT * FROM seats_table where seat_id = ?";
        
        try{
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(selectQuery);
            pst.setLong(1, seatId);
            rs = pst.executeQuery();
            
            if(rs.next()){
                seat.setSeatNo(rs.getString("seat_id"));
                seat.setIsBooked(rs.getBoolean("is_booked"));
                seat.setClassOfService(ServiceClass.valueOf(rs.getString("class_of_service").trim().toUpperCase()));
                seat.setFlight(FlightService.getFlightBasicById(rs.getLong("flight_id")));
                
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
                
                if(conn != null){
                    conn.close();
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
        List<Seat> seats = null;
        
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
                seat.setFlight(FlightService.getFlightBasicById(rs.getLong("flight_id")));
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
            
}
