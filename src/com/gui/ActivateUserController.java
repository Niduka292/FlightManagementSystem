package com.gui;

import com.DTO.CustomerDTO;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class ActivateUserController implements Initializable {

    @FXML
    private AnchorPane activateUserPane;
    
    @FXML
    private TextField tf_email;
    
    @FXML
    private Label lb_errorMsg;
    @FXML
    private Label lb_successMsg;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }    

    @FXML
    void handleBackButtonClick(ActionEvent event) {

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin-logged-in.fxml"));
            AnchorPane pane = loader.load();
            activateUserPane.getChildren().setAll(pane);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    void handleButtonClick(ActionEvent event) {
        ScenesUtil.logoutToLoginPage(activateUserPane);
    }

    @FXML
    void handleEnterButtonClick(ActionEvent event) {

        CustomerDTO customer = null;
        String email = tf_email.getText().trim();
        System.out.println(email);
        UserDTO user = UserService.getUserByEmail(email);
        
        boolean activateSuccess = UserService.activateUser(user.getUserID());
        if(activateSuccess){
            lb_successMsg.setText("User Activated successfully");
        }else{
            lb_errorMsg.setText("Failed to activate user");
        }
        
    }
}
