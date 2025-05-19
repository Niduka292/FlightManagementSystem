package com.gui;

import com.DTO.AirportDTO;
import com.DTO.FlightDTO;
import com.service.AirportService;
import com.service.FlightService;
import com.service.UserService;
import com.util.JDBCUtil;
import com.util.ScenesUtil;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.sql.Connection;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class GenerateReport2Controller implements Initializable {

    @FXML
    private TableView table_report;
    
    @FXML
    private AnchorPane generateReportPane;
    
    @FXML
    private DatePicker dp_date;
    
    @FXML
    private TextField tf_airportCode;
    
    @FXML
    private ComboBox<String> cb_time;
    @FXML
    private TableColumn<Flight, Long> column_flightId;
    @FXML
    private TableColumn<Flight, String> column_from;
    @FXML
    private TableColumn<Flight, String> column_to;
    @FXML
    private TableColumn<Flight, String> column_status;
    @FXML
    private TableColumn<Flight, ZonedDateTime> column_departure;
    
    
    ObservableList<Flight> initialData(){
        
        ZonedDateTime reportTime = convertToZonedDateTime();
        String airportCode = tf_airportCode.getText().trim();
        List<FlightDTO> arrivingFlights = FlightService.getArrivingFlightsForAirport(airportCode, reportTime);
        List<FlightDTO> departingFlights = FlightService.getDepartingFlightsForAirport(airportCode, reportTime);
        
        List<Flight> flightsToDisplay = new ArrayList<>();
        
        for(FlightDTO arriveFlight : arrivingFlights){
            Flight flight = new Flight(arriveFlight.getFlightID(), arriveFlight.getDepartingFrom().getAirportCode(),
                    arriveFlight.getDestination().getAirportCode(), arriveFlight.getDepartureDate(),"ARRIVING");
            flightsToDisplay.add(flight);
        }
        
        for(FlightDTO departingFlight : departingFlights){
            Flight flight = new Flight(departingFlight.getFlightID(), departingFlight.getDepartingFrom().getAirportCode(),
                    departingFlight.getDestination().getAirportCode(), departingFlight.getDepartureDate(), "DEPARTING");
            flightsToDisplay.add(flight);
        }
        return FXCollections.observableArrayList(flightsToDisplay);
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime time = LocalTime.MIDNIGHT;

        while (!time.equals(LocalTime.MIDNIGHT.minusMinutes(30))) {
            cb_time.getItems().add(time.format(formatter));
            time = time.plusMinutes(30);
        }

        cb_time.setPromptText("Select Time");
    }
   
    public ZonedDateTime convertToZonedDateTime() {
        LocalDate selectedDate = dp_date.getValue();
        String timeString = cb_time.getValue(); // e.g., "14:30"

        if (selectedDate == null || timeString == null) {
            throw new IllegalStateException("Date or Time not selected");
        }

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime selectedTime = LocalTime.parse(timeString, timeFormatter);
        String airportCode = tf_airportCode.getText().trim();
        
        Connection conn = JDBCUtil.getConnection();
        AirportDTO airport = AirportService.getAirportById(airportCode, conn);
        String zoneIdString = JDBCUtil.getZoneId(airport.getContinent(), airport.getCity());
        ZonedDateTime zonedDateTime = ZonedDateTime.of(selectedDate, selectedTime,ZoneId.of(zoneIdString));
        System.out.println(zonedDateTime);
        return zonedDateTime;
    }
    
    @FXML
    public void handleEnterButtonClick(ActionEvent event){
        
        column_flightId.setCellValueFactory(new PropertyValueFactory<Flight, Long>("flightId"));
        column_from.setCellValueFactory(new PropertyValueFactory<Flight, String>("departingCode"));
        column_to.setCellValueFactory(new PropertyValueFactory<Flight, String>("destinationCode"));
        column_status.setCellValueFactory(new PropertyValueFactory<Flight, String>("status"));
        column_departure.setCellValueFactory(new PropertyValueFactory<Flight, ZonedDateTime>("departureTime"));
        
        table_report.getItems().clear();
        table_report.setItems(initialData());
    }
    
    @FXML
    void handleBackButtonClick(ActionEvent event) {

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("operator-logged-in.fxml"));
            AnchorPane pane = loader.load();
            generateReportPane.getChildren().setAll(pane);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    void handleButtonClick(ActionEvent event) {
        ScenesUtil.logoutToLoginPage(generateReportPane);
    }
}
