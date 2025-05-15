package com.gui;

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
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class LoginController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button button_login;

    @FXML
    private TextField tf_username;
    @FXML
    private TextField tf_password;
    
    @FXML
    private Label lb_errorMsg;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    
    @FXML
    public void loadSecond(ActionEvent event) {
        
        String username = tf_username.getText();
        String password = tf_password.getText();

        UserDTO user = UserService.userAuthentication(username, password);
        System.out.println(username + " " + password);

        if (user != null) {
            String userType = user.getType();
            System.out.println(userType);
        } else {
            System.out.println("Login failed: User not found or credentials invalid.");
            lb_errorMsg.setText("Invalid username or password.");
        }
        
        String fxmlToLoad = null;

        if (user.getType().equals("admin")) {
            fxmlToLoad = "admin-logged-in.fxml";
        } else if (user.getType().equals("customer")) {
            fxmlToLoad = "customer-logged-in.fxml";
        } else if (user.getType().equals("operator")) {
            fxmlToLoad = "operator-logged-in.fxml";
        } else {
            System.out.println("Unknown user type or authentication failed");
            return;
        }
        
        System.out.println(fxmlToLoad);
        
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlToLoad));
            AnchorPane pane = loader.load();
            
            if(user.getType().equals("customer")) {
                CustomerLoggedInController customerController = loader.getController();
                customerController.showCustomerDetails(username);
            } 
            
            
            if (user.getType().equals("admin")) {
                AdminLoggedInController adminController = loader.getController();
            } 
            
            if(user.getType().equals("operator")){
                OperatorLoggedInController operatorController = loader.getController();
            }
            
            
            rootPane.getChildren().setAll(pane);
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

}
