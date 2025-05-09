package com.DTO;

public class UserDTO {
    
    private String userID;
    private String email;
    private char status; // A - active , I - inactive
    private String username;
    private String password;
    private String type;
    private static int userIdIndex = 1;

    public UserDTO() {
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserDTO(String email, char status, String username, String password) {
        
        String userIdString;
        
        // Format userID into a 8 digit number which generates automatically for each user
        if(userIdIndex < 10){
            userIdString = "0000000";
        }else if(userIdIndex < 100){
            userIdString = "000000";
        }else if(userIdIndex < 1000){
            userIdString = "00000";
        }else if(userIdIndex < 10000){
            userIdString = "0000";
        }else if(userIdIndex < 100000){
            userIdString = "000";
        }else if(userIdIndex < 1000000){
            userIdString = "00";
        }else if(userIdIndex < 1000000){
            userIdString = "0";
        }else{
            userIdString = "";
        }
        
        userID = userIdString + String.valueOf(userIdIndex);
        
        
        
        setUserID(userID);
        setEmail(email);
        setStatus(status);
        setUsername(username);
        setPassword(password);
        userIdIndex++;
    }

    @Override
    public String toString() {
        String msg = "User ID: "+userID;
        msg += "\nEmail: "+email;
        msg += "\nStatus: "+status;
        return msg;
    }
}
