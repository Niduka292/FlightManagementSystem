package com.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class OperatorLoggedInController implements Initializable {

    @FXML
    private AnchorPane operatorLoginPane;
    
    @FXML
    private Button button_logout;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
    }    
    
    @FXML
    public void handleButtonClick(ActionEvent event){
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            AnchorPane pane = loader.load();
            operatorLoginPane.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    @FXML
    public void loadCreateBooking(ActionEvent event){
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("create-booking2.fxml"));
            AnchorPane pane = loader.load();
            operatorLoginPane.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    @FXML
    public void loadGenerateReport(ActionEvent event){
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("generate-reports2.fxml"));
            AnchorPane pane = loader.load();
            operatorLoginPane.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    @FXML
    public void loadViewBokkings(ActionEvent event){
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view-bookings2.fxml"));
            AnchorPane pane = loader.load();
            operatorLoginPane.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    @FXML
    public void loadSearchForFlights(ActionEvent event){
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("search-for-flights2.fxml"));
            AnchorPane pane = loader.load();
            operatorLoginPane.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
}
