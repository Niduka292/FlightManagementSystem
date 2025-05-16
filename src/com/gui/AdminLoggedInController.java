package com.gui;

import com.util.ScenesUtil;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class AdminLoggedInController implements Initializable {

    @FXML
    private AnchorPane adminLoginPane;
    
    @FXML
    private Button button_logout;
    @FXML
    private Button button_createUser;
    @FXML
    private Button button_viewBookings;
    @FXML
    private Button button_updateUser;
    @FXML
    private Button button_deactivateUser;
    @FXML
    private Button button_activateUser;
    @FXML
    private Button button_generateReports;
    @FXML
    private Button button_createBooking;
    @FXML
    private Button button_searchFlights;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    public void handleButtonClick(ActionEvent event){
        
        ScenesUtil.logoutToLoginPage(adminLoginPane);
    }
    
    @FXML
    public void loadCreateUser(ActionEvent event){
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("create-user.fxml"));
            AnchorPane createUserPane = loader.load();
            adminLoginPane.getChildren().setAll(createUserPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void loadViewBookings(ActionEvent event){
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view-user-bookings.fxml"));
            AnchorPane pane = loader.load();
            adminLoginPane.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    @FXML
    public void loadDeactivateUser(ActionEvent event){
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("deactivate-user.fxml"));
            AnchorPane pane = loader.load();
            adminLoginPane.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    @FXML
    public void loadActivateUser(ActionEvent event){
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("activate-user.fxml"));
            AnchorPane pane = loader.load();
            adminLoginPane.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    @FXML
    public void loadUpdateUser(ActionEvent event){
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("update-user.fxml"));
            AnchorPane pane = loader.load();
            adminLoginPane.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
}
