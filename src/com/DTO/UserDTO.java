package com.DTO;

public class UserDTO {
    
    private long userID;
    private String email;
    private char status; // A - active , I - inactive
    private String username;
    private String password;
    protected String type;
    private static int userIdIndex = 1;

    public UserDTO() {
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public UserDTO(String email, char status, String username, String password) {
        
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
