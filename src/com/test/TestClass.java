package com.test;

import com.DTO.*;
import com.service.*;
import com.util.JDBCUtil;
import com.service.AirportService;
import java.sql.Connection;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class TestClass{

    public static void main(String[] args) {
        
        Connection conn = JDBCUtil.getConnection();
        
        
        UserDTO admin1 = new AdministratorDTO("kamal123@gmail.com", 'A');
       
        /*
        if (UserService.createAdmin(admin1)) {
            System.out.println("Admin created.");
        }
*/
        CustomerDTO customer1 = new CustomerDTO("Adam Smith", "P12344507", 25, "adam.smith@example.com", 'A', "adamsmith", "adam123");
        CustomerDTO customer2 = new CustomerDTO("John Doe", "P1234567", 35, "john.doe@example.com", 'A', "johndoe", "john123");

        /*
        boolean customer1Added = UserService.createCustomer(customer1);
        boolean customer2Added = UserService.createCustomer(customer2);

        if (customer1Added && customer2Added) {
            System.out.println("Both customers added successfully.");
        } else {
            System.out.println("Adding customers failed.");
        }
*/
        AirportDTO destinationAirport = new AirportDTO("Heathrow", "London", "UK", "Europe");
        AirportDTO departingAirport = new AirportDTO("Katunayake", "Colombo", "Sri Lanka", "Asia");
        AirportDTO transit1 = new AirportDTO("Chennai", "Chennai", "India", "Asia");
        AirportDTO transit2 = new AirportDTO("Dubai", "Dubai", "UAE", "Asia");

        /*
        boolean airportsAdded = AirportService.addAirport(destinationAirport)
                && AirportService.addAirport(departingAirport)
                && AirportService.addAirport(transit1)
                && AirportService.addAirport(transit2);

        if (airportsAdded) {
            System.out.println("All airports added successfully.");
        } else {
            System.out.println("Airport insertion failed.");
        }
*/
        AircraftDTO aircraft = new AircraftDTO("ARB112", "Embraer E190", 5, 20, 50);
        /*
        if (AircraftService.registerAircraft(aircraft)) {
            System.out.println("Aircraft registered successfully.");
        } else {
            System.out.println("Aircraft registration failed.");
        }
        */
        List<AirportDTO> transitAirports = new ArrayList<>();
        transitAirports.add(transit1);
        transitAirports.add(transit2);

        List<CustomerDTO> customers = new ArrayList<>();
        customers.add(customer1);
        customers.add(customer2);
        
        ZonedDateTime zdt = JDBCUtil.convertStringToZonedDateTime("2025-05-30T15:00:00+05:30[Asia/Colombo]");
        FlightDTO flight = new FlightDTO(zdt, departingAirport, destinationAirport, aircraft, transitAirports);
        
        //flight.setFlightID(11);
        /*
        boolean flightAdded = FlightService.scheduleFlight(flight);
        if (flightAdded) {
            System.out.println("Flight scheduled successfully.");
        } else {
            System.out.println("Flight scheduling failed.");
        }
        */
        
        String seatNo = "F-001";
        Seat seat = SeatService.getSeatBySeatNo(seatNo,flight.getFlightID());
        
        if (seat != null) {
            BookingDTO booking = new BookingDTO(customer1, ServiceClass.FIRST, departingAirport, transit2, flight, seat);
            /*
            boolean bookingResult = BookingService.createBooking(booking);
            if (bookingResult) {
                System.out.println("Booking successful.");
            } else {
                System.out.println("Booking failed. Possible duplicate or seat unavailable.");
            }
            */
        } else {
            System.out.println("Seat not found: " + seatNo);
        }
/*
        UserDTO operator = new OperatorDTO("saman", "saman@xyz.com", 'A', "saman_67", "saman123");
        
        if (UserService.createOperator(operator)) {
            System.out.println("Operator created.");
        } else {
            System.out.println("Operator creation failed.");
        }*/

        
        //UserService.deactivateUser(45L);
        
        AircraftDTO aircraft1 = new AircraftDTO("ARB113", "Boeing 737 MAX", 6, 24, 120);
        AircraftDTO aircraft2 = new AircraftDTO("ARB114", "Airbus A320neo", 8, 30, 150);
        AircraftDTO aircraft3 = new AircraftDTO("ARB115", "Bombardier CRJ900", 4, 18, 90);
        /*
        AircraftService.registerAircraft(aircraft1);
        AircraftService.registerAircraft(aircraft2);
        AircraftService.registerAircraft(aircraft3);
*/
        AirportDTO airport1 = new AirportDTO("John F. Kennedy International Airport", "New York", "USA", "America");
        AirportDTO airport2 = new AirportDTO("Sydney Kingsford Smith Airport", "Sydney", "Australia", "Australia");

        List<AirportDTO> transitList1 = new ArrayList<>();
        transitList1.add(departingAirport);
        transitList1.add(transit1);
        transitList1.add(transit2);
        
        // Base time for spacing flights by a few hours
        ZonedDateTime baseTime = JDBCUtil.convertStringToZonedDateTime("2025-05-25T15:00:00+05:30[Asia/Colombo]");

        ZonedDateTime zdt2 = baseTime.plusHours(3); // 18:00
        //FlightDTO flight2 = new FlightDTO(zdt2, airport1, airport2, aircraft, transitList1);
        //flight2.setFlightID(12);

        ZonedDateTime zdt3 = baseTime.minusDays(25); // 
        FlightDTO flight3 = new FlightDTO(zdt3, departingAirport, transit1, aircraft, transitAirports);
        //flight3.setFlightID(13);

        List<AirportDTO> transitList3 = new ArrayList<>();
        transitList3.add(transit1);
        
        ZonedDateTime zdt4 = baseTime.plusDays(30); // 00:00 next day
        //FlightDTO flight4 = new FlightDTO(zdt4, airport2, transit2, aircraft,transitList3 );
        //flight4.setFlightID(14);

        //boolean flightAdded2 = FlightService.scheduleFlight(flight2);
        boolean flightAdded3 = FlightService.scheduleFlight(flight3);
        //boolean flightAdded4 = FlightService.scheduleFlight(flight4);
        /*
        if (flightAdded2 && flightAdded3 && flightAdded4) {
            System.out.println("Flights scheduled successfully.");
        } else {
            System.out.println("Flights scheduling failed.");
        }*/

        System.out.println(flightAdded3);
        //System.out.println(flightAdded4);
        
    }
    
    public static void insertAllAirports() {
    // Asia airports
    AirportService.addAirport(new AirportDTO("Tokyo International Airport", "Tokyo", "Japan", "Asia"));
    AirportService.addAirport(new AirportDTO("Shanghai Pudong Airport", "Shanghai", "China", "Asia"));
    AirportService.addAirport(new AirportDTO("Seoul Incheon Airport", "Seoul", "South Korea", "Asia"));
    AirportService.addAirport(new AirportDTO("Jakarta Soekarno-Hatta", "Jakarta", "Indonesia", "Asia"));
    AirportService.addAirport(new AirportDTO("Indira Gandhi International Airport", "Delhi", "India", "Asia")); // Default Asia

    // Europe airports
    AirportService.addAirport(new AirportDTO("Charles de Gaulle Airport", "Paris", "France", "Europe"));
    AirportService.addAirport(new AirportDTO("Adolfo Suárez Madrid–Barajas Airport", "Madrid", "Spain", "Europe"));
    AirportService.addAirport(new AirportDTO("Leonardo da Vinci–Fiumicino Airport", "Rome", "Italy", "Europe"));
    AirportService.addAirport(new AirportDTO("Sheremetyevo International Airport", "Moscow", "Russia", "Europe"));
    AirportService.addAirport(new AirportDTO("Berlin Tegel Airport", "Berlin", "Germany", "Europe")); // Default Europe

    // America airports
    AirportService.addAirport(new AirportDTO("O'Hare International Airport", "Chicago", "USA", "America"));
    AirportService.addAirport(new AirportDTO("George Bush Intercontinental Airport", "Houston", "USA", "America"));
    AirportService.addAirport(new AirportDTO("Los Angeles International Airport", "Los Angeles", "USA", "America"));
    AirportService.addAirport(new AirportDTO("Phoenix Sky Harbor International Airport", "Phoenix", "USA", "America"));
    AirportService.addAirport(new AirportDTO("Guarulhos International Airport", "São Paulo", "Brazil", "America"));
    AirportService.addAirport(new AirportDTO("Ministro Pistarini International Airport", "Buenos Aires", "Argentina", "America"));
    AirportService.addAirport(new AirportDTO("John F. Kennedy International Airport", "New York", "USA", "America")); // Default America

    // Africa airports
    AirportService.addAirport(new AirportDTO("Cairo International Airport", "Cairo", "Egypt", "Africa"));
    AirportService.addAirport(new AirportDTO("Jomo Kenyatta International Airport", "Nairobi", "Kenya", "Africa"));
    AirportService.addAirport(new AirportDTO("Murtala Muhammed International Airport", "Lagos", "Nigeria", "Africa"));
    AirportService.addAirport(new AirportDTO("OR.Tambo International Airport", "Johannesburg", "South Africa", "Africa"));

    // Australia/Oceania airports
    AirportService.addAirport(new AirportDTO("Sydney Kingsford Smith Airport", "Sydney", "Australia", "Australia"));
    AirportService.addAirport(new AirportDTO("Perth Airport", "Perth", "Australia", "Australia"));
    AirportService.addAirport(new AirportDTO("Auckland Airport", "Auckland", "New Zealand", "Oceania"));
}

}
