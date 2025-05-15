package com.gui;

import com.DTO.CustomerDTO;
import com.service.UserService;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
    }    
    
    public void handleButtonClick(ActionEvent event){
        
        button_logout.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
                    AnchorPane pane = loader.load();
                    customerLoginPane.getChildren().setAll(pane);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        
    }
    
    
    public void showCustomerDetails(String username){
        
        
        
        CustomerDTO customer = UserService.getCustomerByUsername(username);
        
        lb_user.setText(customer.getName().split(" ")[0]);
        lb_userId.setText(String.valueOf(customer.getUserID()));
        lb_email.setText(customer.getEmail());
        lb_passportno.setText(customer.getPassportNo());
        lb_status.setText(String.valueOf(customer.getStatus()));
        
    }
}


