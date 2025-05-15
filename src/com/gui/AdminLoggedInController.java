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

public class AdminLoggedInController implements Initializable {

    @FXML
    private AnchorPane adminLoginPane;
    
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
                    adminLoginPane.getChildren().setAll(pane);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        
    }
    
}
