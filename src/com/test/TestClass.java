package com.test;

import com.DTO.AdministratorDTO;
import com.DTO.AircraftDTO;
import com.DTO.AirportDTO;
import com.DTO.BookingDTO;
import com.DTO.CustomerDTO;
import com.DTO.FlightDTO;
import com.DTO.Seat;
import com.DTO.ServiceClass;
import com.DTO.UserDTO;
import com.service.AircraftService;
import com.service.AirportService;
import com.service.BookingService;
import com.service.FlightService;
import com.service.SeatService;
import com.service.UserService;
import com.util.JDBCUtil;
import java.sql.Connection;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class TestClass {

    public static void main(String[] args) {

        Connection conn = JDBCUtil.getConnection();
        
        UserDTO admin1 = new AdministratorDTO("kamal123@gmail.com", 'A');
        UserService.createAdmin(admin1);

        CustomerDTO customer1 = new CustomerDTO(
                "Adam Smith",
                "P12344507", 
                25, 
                "adam.smith@example.com", 
                'A', 
                "adamsmith", 
                "adam123" 
        );
        
        CustomerDTO customer2 = new CustomerDTO(
                "John Doe",
                "P1234567", 
                35, 
                "john.doe@example.com", 
                'A', 
                "johndoe", 
                "john123" 
        );

        
        AirportDTO destinationAirport = new AirportDTO("Heathrow", "London", "UK", "Europe");
        AirportDTO departingAirport = new AirportDTO("Katunayake", "Colombo", "Sri Lanka", "Asia");
        boolean airport1Added = AirportService.addAirport(destinationAirport);
        boolean airport2Added = AirportService.addAirport(departingAirport);
        
        
        AirportDTO transit1 = new AirportDTO("Chennai", "Chennai", "India", "Asia");
        AirportDTO transit2 = new AirportDTO("Dubai", "Dubai", "UAE", "Asia");
        boolean transit1Added = AirportService.addAirport(transit1);
        boolean transit2Added = AirportService.addAirport(transit2);
        
        if(airport1Added && airport2Added && transit1Added && transit2Added){
            System.out.println("All airports added successfully.");
        }
        
        
        AircraftDTO aircraft = new AircraftDTO("ARB112", "Embraer E190", 5, 20, 50);
        boolean aircraftAdded = AircraftService.registerAircraft(aircraft);
        
        if(aircraftAdded){
            System.out.println("Aircraft added successfully");
        }
        
        List<AirportDTO> transitAirports = new ArrayList<>();
        transitAirports.add(transit1);
        transitAirports.add(transit2);
        
        List<CustomerDTO> customers = new ArrayList<>();
        boolean customer1Added = UserService.createCustomer(customer1);
        boolean customer2Added = UserService.createCustomer(customer2);

        if (customer1Added && customer2Added) {
            System.out.println("Both customers added successfully.");
        } else {
            System.out.println("Adding customers failed.");
        }

        
        ZonedDateTime zdt = JDBCUtil.convertStringToZonedDateTime("2025-05-08T14:35:22+05:30[Asia/Colombo]");
        FlightDTO flight = new FlightDTO(zdt, departingAirport, destinationAirport, aircraft, transitAirports, customers);
        
        boolean flightAdded = FlightService.scheduleFlight(flight);
        
        Seat seat = SeatService.getSeatById(flight.getFlightID()+"-F-001");
        
        BookingDTO booking = new BookingDTO(customer1, ServiceClass.FIRST, departingAirport, destinationAirport, flight, seat);
        BookingService.createBooking(booking);
        boolean bookingResult = BookingService.createBooking(booking);
        if (bookingResult) {
            System.out.println("Booking successful.");
        } else {
            System.out.println("Booking failed.");
        }

    }
}
