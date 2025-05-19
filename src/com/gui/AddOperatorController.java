package com.gui;

import com.DTO.OperatorDTO;
import com.DTO.UserDTO;
import com.service.UserService;
import com.util.ScenesUtil;
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

public class AddOperatorController implements Initializable {

    @FXML
    private AnchorPane addOperatorPane;
    
    @FXML
    private Button button_back;
    @FXML
    private Button button_logout;
    @FXML
    private Button button_create;
    
    @FXML
    private TextField tf_name;
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
    
    @FXML
    public void addOperator(ActionEvent event){
        
        String name = tf_name.getText();
        String email = tf_email.getText();
        String username = tf_username.getText();
        String password = tf_password.getText();
        
        UserDTO operator = new OperatorDTO(name, email, 'A', username, password);
        
        if(UserService.createOperator(operator)){
            System.out.println("Operator added successfully");
            lb_successMsg.setText("Operator added successfully");
        }else{
            lb_errorMsg.setText("Failed to add operator. Check again");
        }
    }
    
    @FXML
    public void handleButtonClick(ActionEvent event){
        
        ScenesUtil.logoutToLoginPage(addOperatorPane);
    }
    
    @FXML
    public void handleBackButtonClick(ActionEvent event){
        
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("create-user.fxml"));
            AnchorPane pane = loader.load();
            addOperatorPane.getChildren().setAll(pane);
        }catch(IOException e){
            e.printStackTrace();
        }
        
    }
    
}
