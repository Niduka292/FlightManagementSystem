package com.DTO;

public class CustomerDTO extends UserDTO{
    
    private String name;
    private String passportNo;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassportNo() {
        return passportNo;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if(age > 0){ // Valodation for age
            this.age = age;
        }
    }

    public CustomerDTO(){
        
    }
    
    public CustomerDTO(String name,String passportNo, int age,String email, 
            char status, String username, String password) {
        super(email, status, username, password);
        super.type = "customer";
        setName(name);
        setAge(age);
        setPassportNo(passportNo);
        
    }

    @Override
    public String toString() {
        String msg = super.toString();
        msg += "\nAge: "+age;
        msg += "\nPassport No.: "+passportNo;
        
        return msg;
    }
    
    
}
