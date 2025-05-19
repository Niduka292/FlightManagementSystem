package com.gui;

import com.DTO.CustomerDTO;
import com.DTO.UserDTO;
import com.service.UserService;
import com.util.ScenesUtil;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class DeactivateUserController {

    @FXML
    private Label lb_errorMsg;

    @FXML
    private Label lb_successMsg;

    @FXML
    private TextField tf_email;

    @FXML
    private AnchorPane deactivateUserPane;

    @FXML
    void handleBackButtonClick(ActionEvent event) {

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin-logged-in.fxml"));
            AnchorPane pane = loader.load();
            deactivateUserPane.getChildren().setAll(pane);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    void handleButtonClick(ActionEvent event) {
        ScenesUtil.logoutToLoginPage(deactivateUserPane);
    }

    @FXML
    void handleEnterButtonClick(ActionEvent event) {

        CustomerDTO customer = null;
        String email = tf_email.getText().trim();
        System.out.println(email);
        UserDTO user = UserService.getUserByEmail(email);
        
        boolean deactivateSuccess = UserService.deactivateUser(user.getUserID());
        if(deactivateSuccess){
            lb_successMsg.setText("User Deactivated successfully");
        }else{
            lb_errorMsg.setText("Failed to deactivate user");
        }
        
    }

}
