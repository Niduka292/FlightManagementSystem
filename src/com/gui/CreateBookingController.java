package com.gui;

import com.DTO.AirportDTO;
import com.DTO.BookingDTO;
import com.DTO.CustomerDTO;
import com.DTO.FlightDTO;
import com.DTO.Seat;
import com.DTO.ServiceClass;
import com.DTO.UserDTO;
import com.service.AirportService;
import com.service.BookingService;
import com.service.FlightService;
import com.service.SeatService;
import com.service.UserService;
import com.util.JDBCUtil;
import com.util.ScenesUtil;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import java.sql.Connection;

public class CreateBookingController implements Initializable{

    @FXML
    private Button button_back;

    @FXML
    private Button button_logout;

    @FXML
    private ComboBox<ServiceClass> cb_service;

    @FXML
    private AnchorPane createBookingPane;

    @FXML
    private Label lb_errorMsg;

    @FXML
    private Label lb_successMsg;

    @FXML
    private TextField tf_departing;

    @FXML
    private TextField tf_destination;

    @FXML
    private TextField tf_email;

    @FXML
    private TextField tf_flightId;

    @FXML
    private TextField tf_seatNo;

    @FXML
    void handleBackButtonClick(ActionEvent event) {

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin-logged-in.fxml"));
            AnchorPane pane = loader.load();
            createBookingPane.getChildren().setAll(pane);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    void handleButtonClick(ActionEvent event) {
        ScenesUtil.logoutToLoginPage(createBookingPane);
    }
    
    @FXML
    public void handleAddButtonClick(ActionEvent event){
        
        CustomerDTO customer = null;
        
        String email = tf_email.getText().trim();
        UserDTO user  = UserService.getUserByEmail(email);
        if(user.getType().equals("customer")){
            customer = (CustomerDTO) user;
        }
        
        String classOfService = cb_service.getValue().toString();
        
        Connection conn = JDBCUtil.getConnection();
        
        String departingAirportCode = tf_departing.getText().trim();
        AirportDTO departingAirport = AirportService.getAirportById(departingAirportCode, conn);
        
        String destinationAirportCode = tf_destination.getText().trim();
        AirportDTO destinationAirport = AirportService.getAirportById(destinationAirportCode, conn);
        
        String flightIdStr = tf_flightId.getText().trim();
        Long flightId = Long.parseLong(flightIdStr);
        FlightDTO flight = FlightService.getFlightById(flightId);
        
        String seatNo = tf_seatNo.getText().trim();
        Seat seat = SeatService.getSeatBySeatNo(seatNo, flight.getFlightID());
        
        BookingDTO booking = new BookingDTO(customer, ServiceClass.valueOf(classOfService), departingAirport, destinationAirport, flight, seat);
        boolean bookingSuccess = BookingService.createBooking(booking);
        
        if(bookingSuccess){
            lb_successMsg.setText("Booking added successfully");
        }else{
            lb_errorMsg.setText("Failed to add booking");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        cb_service.getItems().addAll(
                ServiceClass.FIRST,
                ServiceClass.BUSINESS,
                ServiceClass.ECONOMY
        );
    }

}
