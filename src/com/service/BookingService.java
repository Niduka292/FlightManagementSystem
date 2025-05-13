package com.service;

import com.DTO.AirportDTO;
import java.sql.*;
import com.util.JDBCUtil;
import com.DTO.BookingDTO;
import com.DTO.FlightDTO;
import com.DTO.ServiceClass;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingService {
    
    public static void createBooking(BookingDTO booking){
        
        Connection conn = null;
        PreparedStatement pst = null;
        
        boolean bookSeat = SeatService.bookSeat(booking.getSeat().getSeatNo(),
                booking.getCustomer(),booking.getFlight().getFlightID(),booking.getDepartingAirport(),booking.getDestination());
        
        String insertQuery = "INSERT INTO bookings_table(booking_date"
                + ",class_of_service,departing_airport_id,destination_airport_id"
                + ",flight_id,seat_id,customer_id) values(?,?,?,?,?,?,?)";
        
        int rowsAffected = 0;
        
        if (booking.getSeat() == null || booking.getFlight() == null || booking.getCustomer() == null) {
            System.out.println("Booking has missing details. Cannot proceed.");
        }
        
        if (!bookSeat) {
            System.out.println("Seat booking failed. Booking not inserted.");
        }

        
        try{
            
            Instant instant = booking.getBookedDate().toInstant();
            Timestamp timestamp = Timestamp.from(instant);
            
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(insertQuery);
            
            pst.setTimestamp(1, timestamp);
            pst.setString(2, booking.getClassOfService().toString());
            pst.setString(3, booking.getDepartingAirport().getAirportCode());
            pst.setString(4, booking.getDestination().getAirportCode());
            pst.setLong(5, booking.getFlight().getFlightID());
            pst.setString(6, booking.getSeat().getSeatNo());
            pst.setLong(7, booking.getCustomer().getUserID());
            
            rowsAffected = pst.executeUpdate();
            
            if(rowsAffected > 0){
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
                                
                AirportDTO departingAirport = AirportService.getAirportById(rs.getString("departing_airport_id"));
                String departingContinent = departingAirport.getContinent();
                String departingCity = departingAirport.getCity();

                Timestamp timestamp = rs.getTimestamp("booking_date");
                
                ZonedDateTime zdt = JDBCUtil.convertTimeStampToZoneDate(departingContinent, departingCity, timestamp);
                
               
                
                BookingDTO booking = new BookingDTO();
                booking.setBookingID(rs.getLong(1));
                booking.setBookedDate(zdt);
                booking.setClassOfService(ServiceClass.valueOf(rs.getString(3)));
                booking.setDepartingAirport(AirportService.getAirportById(rs.getString(4)));
                booking.setDestination(AirportService.getAirportById(rs.getString(5)));
                booking.setFlight(FlightService.getFlightById(rs.getLong(6)));
                booking.setSeat(SeatService.getSeatById(rs.getLong(7)));
                booking.setCustomer(UserService.getCustomerById(rs.getLong(8)));
                
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
    
    
}
