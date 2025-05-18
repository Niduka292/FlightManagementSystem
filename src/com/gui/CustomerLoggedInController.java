package com.gui;

import com.DTO.CustomerDTO;
import com.DTO.UserDTO;
import com.service.UserService;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class CustomerLoggedInController implements Initializable {

    @FXML
    private AnchorPane customerLoginPane;
    
    @FXML
    private Label lb_user;
    @FXML
    private Label lb_userId;
    @FXML
    private Label lb_passportno;
    @FXML
    private Label lb_email;
    @FXML
    private Label lb_status;
    
    @FXML
    private Button button_logout;
        
    private String username;

    public void setUsername(String username) {
        this.username = username;
        showCustomerDetails(username);
    }
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
    }    
    
    @FXML
    public void handleButtonClick(ActionEvent event){
        
       try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            AnchorPane pane = loader.load();
            customerLoginPane.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    public void showCustomerDetails(String username){
        
        CustomerDTO customer = null;
        UserDTO user = UserService.getUserByUsername(username);
        if(user.getType().equals("customer")){
            customer = (CustomerDTO) user;
        }
        
        lb_user.setText(customer.getName().split(" ")[0]);
        lb_userId.setText(String.valueOf(customer.getUserID()));
        lb_email.setText(customer.getEmail());
        lb_passportno.setText(customer.getPassportNo());
        lb_status.setText(String.valueOf(customer.getStatus()));
        
    }
    
    @FXML
    public void loadViewBookings(ActionEvent event){
        
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view-my-bookings.fxml"));
            AnchorPane pane = loader.load();
            ViewMyBookingsController viewMyBookingsController = loader.getController();
            viewMyBookingsController.setUsername(username);            
            ViewMyBookingsController controller = loader.getController();
            controller.setEmail(lb_email.getText());
            
            customerLoginPane.getChildren().setAll(pane);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    @FXML
    public void loadSearchForFlights(ActionEvent event){
        
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("search-for-flight3.fxml"));
            AnchorPane pane = loader.load();
            SearchForFlight3Controller searchForFlightsController = loader.getController();
            searchForFlightsController.setUsername(username);
            
            customerLoginPane.getChildren().setAll(pane);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    
}

