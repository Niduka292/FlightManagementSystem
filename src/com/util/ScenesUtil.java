package com.util;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class ScenesUtil {
    
    public static void logoutToLoginPage(AnchorPane currentPane){
        
        try{
            FXMLLoader loader = new FXMLLoader(ScenesUtil.class.getResource("/com/gui/login.fxml"));
            AnchorPane pane = loader.load();
            currentPane.getChildren().setAll(pane);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
