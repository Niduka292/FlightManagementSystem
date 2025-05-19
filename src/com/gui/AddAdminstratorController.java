package com.gui;

import com.DTO.AdministratorDTO;
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

public class AddAdminstratorController implements Initializable {

    @FXML
    private AnchorPane createAdminPane;
    
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
    public void addAdmin(ActionEvent event){
        
        String email = tf_email.getText();
        
        UserDTO admin = new AdministratorDTO(email, 'A');
        
        if(UserService.createAdmin(admin)){
            System.out.println("Administrator added successfully");
            lb_successMsg.setText("Administrator added successfully");
        }else{
            lb_errorMsg.setText("Failed to add administrator. Check again");
        }
    }
    
    @FXML
    public void handleButtonClick(ActionEvent event){
        
        ScenesUtil.logoutToLoginPage(createAdminPane);
    }
    
    @FXML
    public void handleBackButtonClick(ActionEvent event){
        
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("create-user.fxml"));
            AnchorPane pane = loader.load();
            createAdminPane.getChildren().setAll(pane);
        }catch(IOException e){
            e.printStackTrace();
        }
        
    }
    
}
