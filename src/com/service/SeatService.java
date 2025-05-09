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

public class SeatService {
    
    public static void addSeat(Seat seat){
        
        Connection conn = null;
        PreparedStatement pst = null;
        
        String insertQuery = "INSERT INTO seats_table(seat_id, is_booked, class_of_service, flight_id) VALUES(?,?,?,?)";
        
        try{
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(insertQuery);
            pst.setString(1, seat.getSeatNo());
            pst.setBoolean(2, seat.getIsBooked());
            pst.setString(3, seat.getClassOfService().toString());
            pst.setString(4, seat.getFlight().getFlightID());
            
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
    
    public static boolean checkIsSeatBooked(String seatId){
        
        Connection conn = null;
        PreparedStatement pst = null;
        boolean isBooked = false;
        ResultSet rs =  null;
        
        String selectQuery = "SELECT is_booked FROM seats_table where seat_id = ?";
        
        try{
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(selectQuery);
            pst.setString(1, seatId);
            rs = pst.executeQuery();
            
            if(rs.next()){
                isBooked = rs.getBoolean("is_booked");
                if(isBooked){
                    System.out.println("Seat "+seatId+"is booked");
                }
            }else{
                System.out.println("Seat "+seatId+" was mot founf");
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
    
    public static boolean bookSeat(String seatNo, CustomerDTO customer, String flightId,
            AirportDTO origin, AirportDTO destination){
        
        String seatId = flightId+"-"+seatNo;
        boolean isPrevBooked = checkIsSeatBooked(seatId);
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean isNowBooked = false;
        ZonedDateTime zdtNow = ZonedDateTime.now();
        Seat seat = getSeatById(seatId);
        FlightDTO flight = FlightService.getFlightById(flightId);
        
        String selectQuery = "SELECT is_booked FROM seats_table where seat_id = ?";
                
        
        if(!isPrevBooked){
            if(seat.getFlight().getDepartureDate().minusMinutes(30).isAfter(zdtNow)){
                try{
                    conn = JDBCUtil.getConnection();
                    pst = conn.prepareStatement(selectQuery);
                    pst.setString(1, seatId);
                    rs = pst.executeQuery();
                    
                    if(rs.next() && !rs.getBoolean("is_booked")){
                        isNowBooked = true;
                        BookingDTO booking = new BookingDTO();
                        booking.setCustomer(customer);
                        booking.setSeat(seat);
                        booking.setFlight(flight);
                        booking.setClassOfService(seat.getClassOfService());
                        booking.setBookedDate(zdtNow);
                        booking.setDepartingAirport(origin);
                        booking.setDestination(destination);
                        BookingService.createBooking(booking);
                    }else{
                        System.out.println("Seat is already booked.");
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
            }
        }else{
            isNowBooked = true;
        }
        
        return isNowBooked;
    }
    
    public static Seat getSeatById(String seatId){
        
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs =  null;
        Seat seat = new Seat();
        
        String selectQuery = "SELECT * FROM seats_table where seat_id = ?";
        
        try{
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(selectQuery);
            pst.setString(1, seatId);
            rs = pst.executeQuery();
            
            if(rs.next()){
                seat.setSeatNo(rs.getString("seat_id"));
                seat.setIsBooked(rs.getBoolean("is_booked"));
                seat.setClassOfService(ServiceClass.valueOf(rs.getString("class_of_service").trim().toUpperCase()));
                seat.setFlight(FlightService.getFlightById(rs.getString("flight_id")));
                
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
            
}
