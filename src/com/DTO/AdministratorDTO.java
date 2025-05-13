package com.DTO;

public class AdministratorDTO extends UserDTO{

    public AdministratorDTO(String email, char status) {
        super(email, status, "Admin", "admin123");
        super.type = "admin";
    }
    
    public AdministratorDTO(){
        
    }
    
}
