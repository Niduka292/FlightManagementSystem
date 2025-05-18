package com.gui;

import com.util.ScenesUtil;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class UpdateUserController {

    @FXML
    private AnchorPane updateUserPane;

    @FXML
    void handleBackButtonClick(ActionEvent event) {

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin-logged-in.fxml"));
            AnchorPane pane = loader.load();
            updateUserPane.getChildren().setAll(pane);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    void handleButtonClick(ActionEvent event) {
        ScenesUtil.logoutToLoginPage(updateUserPane);
    }
    
    @FXML
    void loadUpdateEmail(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("update_email.fxml"));
            AnchorPane pane = loader.load();
            updateUserPane.getChildren().setAll(pane);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    void loadUpdateAge(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("update-age.fxml"));
            AnchorPane pane = loader.load();
            updateUserPane.getChildren().setAll(pane);
        }catch(IOException e){
            e.printStackTrace();
        }
        
    }

    @FXML
    void loadUpdateName(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("update-name.fxml"));
            AnchorPane pane = loader.load();
            updateUserPane.getChildren().setAll(pane);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    @FXML
    void loadUpdatePassportNo(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("update-passport-no.fxml"));
            AnchorPane pane = loader.load();
            updateUserPane.getChildren().setAll(pane);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

}
