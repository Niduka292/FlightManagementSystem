package com.gui;

import com.util.ScenesUtil;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

public class CreateUserController implements Initializable {
    
    @FXML
    private AnchorPane createUserPane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }   
    
    @FXML
    public void handleButtonClick(ActionEvent event){
        
        ScenesUtil.logoutToLoginPage(createUserPane);
    }
    
    @FXML
    public void loadAddCustomer(ActionEvent event){
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add-customer.fxml"));
            AnchorPane createCustomerPane = loader.load();
            createUserPane.getChildren().setAll(createCustomerPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void loadAddOperator(ActionEvent event){
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add-operator.fxml"));
            AnchorPane createOperatorPane = loader.load();
            createUserPane.getChildren().setAll(createOperatorPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void loadAddAdministrator(ActionEvent event){
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add-adminstrator.fxml"));
            AnchorPane createAdminPane = loader.load();
            createUserPane.getChildren().setAll(createAdminPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void handleBackButtonClick(ActionEvent event){
        
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin-logged-in.fxml"));
            AnchorPane pane = loader.load();
            createUserPane.getChildren().setAll(pane);
        }catch(IOException e){
            e.printStackTrace();
        }
        
    }
    
    
}
