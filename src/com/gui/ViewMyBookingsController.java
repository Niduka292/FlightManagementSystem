package com.gui;

import com.DTO.BookingDTO;
import com.DTO.CustomerDTO;
import com.DTO.UserDTO;
import com.service.BookingService;
import com.service.SeatService;
import com.service.UserService;
import com.util.ScenesUtil;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class ViewMyBookingsController implements Initializable{

    @FXML
    private AnchorPane viewBookingsPane;   
    
    @FXML
    private TableColumn<Booking, LocalDate> column_date;

    @FXML
    private TableColumn<Booking, String> column_class;

    @FXML
    private TableColumn<Booking, String> column_from;

    @FXML
    private TableColumn<Booking, String> column_seatNo;

    @FXML
    private TableColumn<Booking, String> column_to;

    private String email;

    public void setEmail(String email) {
        this.email = email;
        loadBookingsFor(email);
    }
    
    @FXML
    private TableView<Booking> table_viewBokkings;
    
    @FXML
    void handleButtonClick(ActionEvent event) {
        ScenesUtil.logoutToLoginPage(viewBookingsPane);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        
    }

    @FXML
    public void handleBackButtonClick(ActionEvent event){
        
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("customer-logged-in.fxml"));
            AnchorPane pane = loader.load();
            viewBookingsPane.getChildren().setAll(pane);
        }catch(IOException e){
            e.printStackTrace();
        }
       
    }
    
    public void loadBookingsFor(String email) {
        
        column_date.setCellValueFactory(new PropertyValueFactory<>("bookedDate"));
        column_class.setCellValueFactory(new PropertyValueFactory<>("classOfService"));
        column_from.setCellValueFactory(new PropertyValueFactory<>("departedAirportCode"));
        column_to.setCellValueFactory(new PropertyValueFactory<>("destinationAirportCode"));
        column_seatNo.setCellValueFactory(new PropertyValueFactory<>("seatNo"));

        CustomerDTO customer = null;
        UserDTO user = UserService.getUserByEmail(email);
        if (user != null && user.getType().equalsIgnoreCase("customer")) {
            customer = (CustomerDTO) user;
        }

        if (customer == null) {
            System.out.println("Invalid customer email: " + email);
            return;
        }

        List<BookingDTO> listOfBookings = BookingService.viewBookingsByCustomer(customer.getUserID());
        List<Booking> bookingsToDisplay = new ArrayList<>();

        for (BookingDTO booking : listOfBookings) {
            Booking b = new Booking(
                booking.getBookedDate().toLocalDate(),
                booking.getClassOfService().toString(),
                booking.getDepartingAirport().getAirportCode(),
                booking.getDestination().getAirportCode(),
                SeatService.getSeatNoById(booking.getSeat().getSeatId())
            );
            bookingsToDisplay.add(b);
        }

        ObservableList<Booking> observableBookings = FXCollections.observableArrayList(bookingsToDisplay);
        table_viewBokkings.setItems(observableBookings);
    }

    
}

