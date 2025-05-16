package com.gui;

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

public class UpdateAgeController implements Initializable {

    @FXML
    private AnchorPane updateAgePane;
    
    @FXML
    private TextField tf_userId;
    @FXML
    private TextField tf_age;
    
    @FXML
    private Label lb_errorMsg1;
    @FXML
    private Label lb_successMsg1;   
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
 
    @FXML
    public void handleEnterButtonClick(ActionEvent event){
        
        String ageStr = tf_age.getText().trim();
        String strUserId = tf_userId.getText().trim();
        
        Long userId = Long.parseLong(strUserId);
        int age = Integer.parseInt(ageStr.trim());
        
        boolean updateSuccess = UserService.updateCustomerAge(age, userId);
        
        if(updateSuccess){
            lb_successMsg1.setText("Customer age updated successfully.");
        }else{
            lb_errorMsg1.setText("Invalid user ID");
        }
        
        
    }
    
    @FXML
    void handleBackButtonClick(ActionEvent event) {

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("update-user.fxml"));
            AnchorPane pane = loader.load();
            updateAgePane.getChildren().setAll(pane);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    void handleButtonClick(ActionEvent event) {
        ScenesUtil.logoutToLoginPage(updateAgePane);
    }
}
