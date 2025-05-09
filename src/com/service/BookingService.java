package com.service;

import com.DTO.AirportDTO;
import java.sql.*;
import com.util.JDBCUtil;
import com.DTO.BookingDTO;
import com.DTO.ServiceClass;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingService {
    
    public static boolean createBooking(BookingDTO booking){
        
        Connection conn = null;
        PreparedStatement pst = null;
        
        boolean bookSeat = SeatService.bookSeat(booking.getSeat().getSeatNo(),
                booking.getCustomer(),booking.getFlight().getFlightID(),booking.getDepartingAirport(),booking.getDestination());
        
        String insertQuery = "INSERT INTO bookings_table(booking_id,booking_date"
                + ",class_of_service,departing_airport_id,destination_airport_id"
                + ",flight_id,seat_id,customer_id) values(?,?,?,?,?,?,?,?)";
        
        boolean bookingAdded = false;
        int rowsAffected = 0;
        
        if (booking.getSeat() == null || booking.getFlight() == null || booking.getCustomer() == null) {
            System.out.println("Booking has missing details. Cannot proceed.");
            return false;
        }
        
        if (!bookSeat) {
            System.out.println("Seat booking failed. Booking not inserted.");
            return false;
        }

        
        try{
            
            Instant instant = booking.getBookedDate().toInstant();
            Timestamp timestamp = Timestamp.from(instant);
            
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(insertQuery);
            
            pst.setString(1, booking.getBookingID());
            pst.setTimestamp(2, timestamp);
            pst.setString(3, booking.getClassOfService().toString());
            pst.setString(4, booking.getDepartingAirport().getAirportID());
            pst.setString(5, booking.getDestination().getAirportID());
            pst.setString(6, booking.getFlight().getFlightID());
            pst.setString(7, booking.getSeat().getSeatNo());
            pst.setString(8, booking.getCustomer().getUserID());
            
            rowsAffected = pst.executeUpdate();
            
            if(rowsAffected > 0){
                bookingAdded = true;
                System.out.println("Seat "+booking.getSeat().getSeatNo()+" of flight "+
                booking.getFlight().getFlightID()+" was booked successfully.");
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
        
        return bookingAdded;
    }
    
    public static List<BookingDTO> displayAllBookings(){
        
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        List<BookingDTO> bookings = new ArrayList<>();
        
        String selectQuery = "SELECT * FROM bookings_table";
        try{
            conn = JDBCUtil.getConnection();
            st = conn.createStatement();
            rs = st.executeQuery(selectQuery);
            
            while(rs.next()){
                /*
                Timestamp ts = Timestamp.valueOf(rs.getTimeStamp(2));
                Instant instant = ts.toInstant();
                */
                
                AirportDTO departingAirport = getAirportById(rs.getString("departing_airport_id"));
                String departingContinent = departingAirport.getContinent();
                String departingCity = departingAirport.getCity();

                Timestamp timestamp = rs.getTimestamp("booking_date");
                
                ZonedDateTime zdt = JDBCUtil.convertTimeStampToZoneDate(departingContinent, departingCity, timestamp);
                
               
                
                BookingDTO booking = new BookingDTO();
                booking.setBookingID(rs.getString(1));
                booking.setBookedDate(zdt);
                booking.setClassOfService(ServiceClass.valueOf(rs.getString(3)));
                booking.setDepartingAirport(getAirportById(rs.getString(4)));
                booking.setDestination(getAirportById(rs.getString(5)));
                booking.setFlight(FlightService.getFlightById(rs.getString(6)));
                booking.setSeat(SeatService.getSeatById(rs.getString(7)));
                booking.setCustomer(UserService.getCustomerById(rs.getString(8)));
                
                bookings.add(booking);
            }
            
            
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(rs != null){
                    rs.close();
                }
                
                if(st != null){
                    st.close();
                }
                
                if(conn != null){
                    conn.close();
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return bookings;
    }
    
    public static AirportDTO getAirportById(String airportId){
        
        AirportDTO airport = null;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        String selectQuery = "SELECT * FROM airports_table WHERE airport_id = ?";
        
        try{
            
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(selectQuery);
            pst.setString(1, airportId);
            rs = pst.executeQuery();
            
            while(rs.next()){
                airport = new AirportDTO();
                airport.setAirportID(rs.getString(1));
                airport.setAirportName(rs.getString(2));
                airport.setCity(rs.getString(3));
                airport.setCountry(rs.getString(4));
                airport.setContinent(rs.getString(5));
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
        
        return airport;
        
    }
    
    
    
}
