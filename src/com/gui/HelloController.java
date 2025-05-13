package com.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class HelloController {
    
    @FXML
    private Button button;  
    
    @FXML
    private void handleButtonClick() {
        System.out.println("Hello World! Button was clicked!");
        
    }
}