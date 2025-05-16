package com.gui;

import com.DTO.AirportDTO;
import com.DTO.FlightDTO;
import com.service.AirportService;
import com.service.FlightService;
import com.util.JDBCUtil;
import com.util.ScenesUtil;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
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

public class SearchForFlightsController implements Initializable {

    @FXML
    private AnchorPane searchFlightPane;
    @FXML
    private DatePicker dp_startDate;
    @FXML
    private DatePicker dp_endDate;
    @FXML
    private ComboBox<String> cb_startTime;
    @FXML
    private ComboBox<String> cb_endTime;
    
    @FXML
    private TextField tf_origin;
    @FXML
    private TextField tf_destination;
    
    @FXML
    private TableView<Flight2> table_report;
    
    @FXML
    private TableColumn<Flight2, Long> column_flightId;
    @FXML
    private TableColumn<Flight2, String> column_from;
    @FXML
    private TableColumn<Flight2, String> column_to;
    @FXML
    private TableColumn<Flight2, ZonedDateTime> column_departure;
    
    ObservableList<Flight2> initialData(){
        
        String origin = tf_origin.getText().trim();
        String destination = tf_destination.getText().trim();
        
        ZonedDateTime startTime = convertToZonedDateTime(dp_startDate,cb_startTime, origin);
        ZonedDateTime endTime = convertToZonedDateTime(dp_endDate, cb_endTime, destination);
        
        List<FlightDTO> directFlights = FlightService.getDirectFlightsAvailable(origin, destination, startTime, endTime);
        List<FlightDTO> transitFlights = FlightService.getTransitFlightsAvailable(origin, destination, startTime, endTime);
        
        List<Flight2> flightsToDisplay = new ArrayList<>();
        
        for(FlightDTO flightDTO : directFlights){
            
            Flight2 flight = new Flight2(flightDTO.getFlightID(), flightDTO.getDepartingFrom().getAirportCode(),
                    flightDTO.getDestination().getAirportCode(), flightDTO.getDepartureDate());
            flightsToDisplay.add(flight);
        }
        
        for(FlightDTO flightDTO : transitFlights){
            
            Flight2 flight = new Flight2(flightDTO.getFlightID(), flightDTO.getDepartingFrom().getAirportCode(),
                    flightDTO.getDestination().getAirportCode(), flightDTO.getDepartureDate());
            flightsToDisplay.add(flight);
        }
        
        return FXCollections.observableArrayList(flightsToDisplay);
    }
    
    public ZonedDateTime convertToZonedDateTime(DatePicker dp, ComboBox<String> cb, String airportCode) {
        LocalDate selectedDate = dp.getValue();
        String timeString = cb.getValue(); // e.g., "14:30"

        if (selectedDate == null || timeString == null) {
            throw new IllegalStateException("Date or Time not selected");
        }

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime selectedTime = LocalTime.parse(timeString, timeFormatter);
        
        Connection conn = JDBCUtil.getConnection();
        AirportDTO airport = AirportService.getAirportById(airportCode, conn);
        String zoneIdString = JDBCUtil.getZoneId(airport.getContinent(), airport.getCity());
        ZonedDateTime zonedDateTime = ZonedDateTime.of(selectedDate, selectedTime,ZoneId.of(zoneIdString));
        System.out.println(zonedDateTime);
        return zonedDateTime;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime time = LocalTime.MIDNIGHT;

        while (!time.equals(LocalTime.MIDNIGHT.minusMinutes(30))) {
            cb_startTime.getItems().add(time.format(formatter));
            time = time.plusMinutes(30);
        }
        
        time = LocalTime.MIDNIGHT;

        cb_startTime.setPromptText("Select Time");
        
        while (!time.equals(LocalTime.MIDNIGHT.minusMinutes(30))) {
            cb_endTime.getItems().add(time.format(formatter));
            time = time.plusMinutes(30);
        }

        cb_endTime.setPromptText("Select Time");
    }   
    
    @FXML
    void handleBackButtonClick(ActionEvent event) {

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin-logged-in.fxml"));
            AnchorPane pane = loader.load();
            searchFlightPane.getChildren().setAll(pane);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    void handleButtonClick(ActionEvent event) {
        ScenesUtil.logoutToLoginPage(searchFlightPane);
    }
    
    @FXML
    public void handleEnterButtonClick(ActionEvent event){
        
        column_flightId.setCellValueFactory(new PropertyValueFactory<Flight2, Long>("flightId"));
        column_from.setCellValueFactory(new PropertyValueFactory<Flight2, String>("originAirportCode"));
        column_to.setCellValueFactory(new PropertyValueFactory<Flight2, String>("destinationAirportCode"));
        column_departure.setCellValueFactory(new PropertyValueFactory<Flight2, ZonedDateTime>("departureDate"));
        
        table_report.getItems().clear();
        table_report.setItems(initialData());
    }
    
}
