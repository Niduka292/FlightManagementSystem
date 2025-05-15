package com.gui;

import com.DTO.CustomerDTO;
import com.DTO.UserDTO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import com.service.UserService;
import com.util.ScenesUtil;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;

public class AddCustomerController implements Initializable {

    @FXML
    private AnchorPane addCustomerPane;
    
    @FXML
    private Button button_back;
    @FXML
    private Button button_logout;
    @FXML
    private Button button_create;
    
    @FXML
    private TextField tf_name;
    @FXML
    private TextField tf_passportNo;
    @FXML
    private TextField tf_age;
    @FXML
    private TextField tf_email;
    @FXML
    private TextField tf_username;
    @FXML
    private TextField tf_password;
    
    @FXML
    private Label lb_errorMsg;
    @FXML
    private Label lb_successMsg;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }   
    
    public void createCustomer(ActionEvent event){
        
        String name = tf_name.getText();
        String passportNo = tf_passportNo.getText();
        int age = Integer.parseInt(tf_age.getText());
        String email = tf_email.getText();
        String username = tf_username.getText();
        String password = tf_password.getText();
        
        UserDTO customer = new CustomerDTO(name, passportNo, age, email, 'A', username, password);
        
        if(UserService.createCustomer(customer)){
            System.out.println("Customer added successfully");
            lb_successMsg.setText("Customer added successfully");
        }else{
            lb_errorMsg.setText("Failed to add customer. Check again");
        }
        
    }
    
    @FXML
    public void handleButtonClick(ActionEvent event){
        
        ScenesUtil.logoutToLoginPage(addCustomerPane);
    }
    
    @FXML
    public void handleBackButtonClick(ActionEvent event){
        
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("create-user.fxml"));
            AnchorPane pane = loader.load();
            addCustomerPane.getChildren().setAll(pane);
        }catch(IOException e){
            e.printStackTrace();
        }
        
    }
}
