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

public class ViewBookings2Controller implements Initializable{

    @FXML
    private AnchorPane viewBookingsPane;
    
    @FXML
    private Label lb_errorMsg;
    
    @FXML
    private TextField tf_email;
    
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


    ObservableList<Booking> initialData(){
        
        CustomerDTO customer = null;
        String email = tf_email.getText().trim();
        UserDTO user = UserService.getUserByEmail(email);
        if(user.getType().equals("customer")){
            customer = (CustomerDTO) user;
        }
        
        if(customer == null){
            lb_errorMsg.setText("Invalid email. Try again");
        }
        
        List<BookingDTO> listOfBookings = BookingService.viewBookingsByCustomer(customer.getUserID());
        
        List<Booking> bookingsToDisplay = new ArrayList<>();
        for(BookingDTO booking : listOfBookings){
            Booking b = new Booking(booking.getBookedDate().toLocalDate(),booking.getClassOfService().toString(),
                    booking.getDepartingAirport().getAirportCode(),booking.getDestination().getAirportCode(),
                    SeatService.getSeatNoById(booking.getSeat().getSeatId()));
            bookingsToDisplay.add(b);
        }
        
        return FXCollections.observableArrayList(bookingsToDisplay);
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
    public void handleEnterButtonClick(ActionEvent event){
        
        column_date.setCellValueFactory(new PropertyValueFactory<Booking, LocalDate>("bookedDate"));
        column_class.setCellValueFactory(new PropertyValueFactory<Booking, String>("classOfService"));
        column_from.setCellValueFactory(new PropertyValueFactory<Booking, String>("departedAirportCode"));
        column_to.setCellValueFactory(new PropertyValueFactory<Booking, String>("destinationAirportCode"));
        column_seatNo.setCellValueFactory(new PropertyValueFactory<Booking, String>("seatNo"));
        
        table_viewBokkings.getItems().clear();
        table_viewBokkings.setItems(initialData());
    }
    
    @FXML
    public void handleBackButtonClick(ActionEvent event){
        
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("operator-logged-in.fxml"));
            AnchorPane pane = loader.load();
            viewBookingsPane.getChildren().setAll(pane);
        }catch(IOException e){
            e.printStackTrace();
        }
        
    }
}
