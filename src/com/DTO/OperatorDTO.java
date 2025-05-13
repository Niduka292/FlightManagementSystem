package com.DTO;

public class OperatorDTO extends UserDTO{
    
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OperatorDTO(){
        
    }
    
    public OperatorDTO(String name, String email, char status, 
            String username, String password) {
        
        super(email, status, username, password);
        setName(name);
        super.type = "operator";
    }    
    
}
