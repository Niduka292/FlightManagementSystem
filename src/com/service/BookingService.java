package com.service;

import com.DTO.AirportDTO;
import java.sql.*;
import com.util.JDBCUtil;
import com.DTO.BookingDTO;
import com.DTO.FlightDTO;
import com.DTO.ServiceClass;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingService {
        
    public static boolean createBooking(BookingDTO booking){
        if(booking == null || booking.getSeat() == null || booking.getFlight() == null || booking.getCustomer() == null){
            System.out.println("Booking has missing details. Cannot proceed.");
            return false;
        }

        FlightDTO flight = booking.getFlight();
        ZonedDateTime departureTime = flight.getDepartureDate();
        ZonedDateTime now = ZonedDateTime.now();

        if(!departureTime.minusMinutes(30).isAfter(now)){
            System.out.println("Cannot book seat less than 30 minutes before departure.");
            return false;
        }

        boolean seatBooked = SeatService.bookSeat(booking.getSeat().getSeatNo(), flight.getFlightID());
        if(!seatBooked){
            System.out.println("Seat booking failed. Booking not inserted.");
            return false;
        }

        Connection conn = null;
        PreparedStatement pst = null;
        boolean bookingInserted = false;

        try{
            conn = JDBCUtil.getConnection();

            String insertQuery = "INSERT INTO bookings_table(booking_date,class_of_service,"
                    + "departing_airport_id,destination_airport_id,flight_id,seat_id,"
                    + "customer_id) VALUES (?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(insertQuery);

            Timestamp timestamp = Timestamp.from(booking.getBookedDate().toInstant());

            pst.setTimestamp(1, timestamp);
            pst.setString(2, booking.getClassOfService().toString());
            pst.setString(3, booking.getDepartingAirport().getAirportCode());
            pst.setString(4, booking.getDestination().getAirportCode());
            pst.setLong(5, FlightService.getFlightIdByDetails(flight));
            pst.setLong(6, booking.getSeat().getSeatId());
            pst.setLong(7, booking.getCustomer().getUserID());

            bookingInserted = pst.executeUpdate() > 0;

            if(bookingInserted){
                System.out.println("Seat " + booking.getSeat().getSeatNo() +
                        " of flight " + flight.getFlightID() + " was booked successfully.");
            }

        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try {
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

        return bookingInserted;
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
                                
                AirportDTO departingAirport = AirportService.getAirportById(rs.getString("departing_airport_id"),conn);
                AirportDTO destinationAirport = AirportService.getAirportById(rs.getString("destination_airport_id"),conn);
                
                String departingContinent = departingAirport.getContinent();
                String departingCity = departingAirport.getCity();

                Timestamp timestamp = rs.getTimestamp("booking_date");
                
                ZonedDateTime zdt = JDBCUtil.convertTimeStampToZoneDate(departingContinent, departingCity, timestamp);
                
                
                BookingDTO booking = new BookingDTO();
                booking.setBookingID(rs.getLong(1));
                booking.setBookedDate(zdt);
                booking.setClassOfService(ServiceClass.valueOf(rs.getString(3)));
                booking.setDepartingAirport(departingAirport);
                booking.setDestination(destinationAirport);
                booking.setFlight(FlightService.getFlightById(rs.getLong(6)));
                booking.setSeat(SeatService.getSeatById(rs.getLong(7),conn));
                booking.setCustomer(UserService.getCustomerById(rs.getLong(8),conn));
                
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
    
    public static List<BookingDTO> viewBookingsByCustomer(long customerId){
        
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<BookingDTO> bookings = new ArrayList<>();
        
        String selectQuery = "SELECT * FROM bookings_table WHERE customer_id = ?";
        
        try{
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(selectQuery);
            rs = pst.executeQuery();
            
            while(rs.next()){
                                
                AirportDTO departingAirport = AirportService.getAirportById(rs.getString("departing_airport_id"),conn);
                AirportDTO destinationAirport = AirportService.getAirportById(rs.getString("destination_airport_id"),conn);
                
                String departingContinent = departingAirport.getContinent();
                String departingCity = departingAirport.getCity();

                Timestamp timestamp = rs.getTimestamp("booking_date");
                
                ZonedDateTime zdt = JDBCUtil.convertTimeStampToZoneDate(departingContinent, departingCity, timestamp);
                
                
                BookingDTO booking = new BookingDTO();
                booking.setBookingID(rs.getLong(1));
                booking.setBookedDate(zdt);
                booking.setClassOfService(ServiceClass.valueOf(rs.getString(3)));
                booking.setDepartingAirport(departingAirport);
                booking.setDestination(destinationAirport);
                booking.setFlight(FlightService.getFlightById(rs.getLong(6)));
                booking.setSeat(SeatService.getSeatById(rs.getLong(7),conn));
                booking.setCustomer(UserService.getCustomerById(rs.getLong(8),conn));
                
                bookings.add(booking);
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
        
        return bookings;
        
    }
    
    
    
}
