package com.service;

import com.DTO.AdministratorDTO;
import com.DTO.CustomerDTO;
import com.DTO.OperatorDTO;
import com.DTO.UserDTO;
import java.sql.*;
import com.util.JDBCUtil;
import com.util.UserNotFoundException;

public class UserService {
    
   
    public static boolean createCustomer(UserDTO user){
        
        Connection conn = null;
        PreparedStatement pst = null;
        int rowsAffected = 0;
        CustomerDTO customer = null;
        boolean customerAdded = false;
        
        String insertQuery = "INSERT INTO users_table(email,username,"
                + "password,name,passport_no,status,age,user_type) values(?,?,?,?,?,?,?,?)";        
        
        if(user instanceof CustomerDTO){
            customer = (CustomerDTO) user;
        }else{
            System.out.println("Invalid user type provided.");
        }
        
        try{
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(insertQuery);
            pst.setString(1, customer.getEmail());
            pst.setString(2, customer.getUsername());
            pst.setString(3, customer.getPassword());
            pst.setString(4, customer.getName());
            pst.setString(5, customer.getPassportNo());
            pst.setString(6, String.valueOf(customer.getStatus()));
            pst.setString(7, String.valueOf(customer.getAge()));
            pst.setString(8, customer.getType());
            rowsAffected = pst.executeUpdate();
            
            if(rowsAffected > 0){
                System.out.println("Customer added to the system successfully.");
                customerAdded = true;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(pst != null){
                    pst.close();
                }
                
                if(conn != null){
                    conn.close();
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return customerAdded;
    }
    
    public static boolean createOperator(UserDTO user){
        
        Connection conn = null;
        PreparedStatement pst = null;
        int rowsAffected = 0;
        OperatorDTO operator = null;
        boolean operatorAdded = false;
        
        String insertQuery = "INSERT INTO users_table(email,username,"
                + "password,name,passport_no,status,age,user_type) values(?,?,?,?,?,?,?,?)";        
        
        if(user instanceof OperatorDTO){
            operator = (OperatorDTO) user;
        }else{
            System.out.println("Invalid user type provided.");
        }
        
        try{
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(insertQuery);
            pst.setString(1, operator.getEmail());
            pst.setString(2, operator.getUsername());
            pst.setString(3, operator.getPassword());
            pst.setString(4, operator.getName());
            pst.setString(5, "-");
            pst.setString(6, String.valueOf(operator.getStatus()));
            pst.setString(7, "-");
            pst.setString(8, operator.getType());
            rowsAffected = pst.executeUpdate();
            
            if(rowsAffected > 0){
                System.out.println("Operator added to the system successfully.");
                operatorAdded = true;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(pst != null){
                    pst.close();
                }
                
                if(conn != null){
                    conn.close();
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return operatorAdded;
    }
    
    
    public static boolean createAdmin(UserDTO user){
        
        Connection conn = null;
        PreparedStatement pst = null;
        int rowsAffected = 0;
        AdministratorDTO admin = null;
        boolean adminAdded = false;
        
        String insertQuery = "INSERT INTO users_table(email,username,"
                + "password,name,passport_no,status,age,user_type) values(?,?,?,?,?,?,?,?)";        
        
        if(user instanceof AdministratorDTO){
            admin = (AdministratorDTO) user;
        }else{
            System.out.println("Invalid user type provided.");
        }
        
        try{
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(insertQuery);
            pst.setString(1, admin.getEmail());
            pst.setString(2, admin.getUsername());
            pst.setString(3, admin.getPassword());
            pst.setString(4, "-");
            pst.setString(5, "-");
            pst.setString(6, String.valueOf(admin.getStatus()));
            pst.setString(7, "-");
            pst.setString(8, admin.getType());
            rowsAffected = pst.executeUpdate();
            
            if(rowsAffected > 0){
                System.out.println("Administrator added to the system successfully.");
                adminAdded = true;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{                
                if(pst != null){
                    pst.close();
                }
                
                if(conn != null){
                    conn.close();
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return adminAdded;
    }
    
    
    
    public static CustomerDTO getCustomerById(long userId, Connection conn){
        
        UserDTO user = new UserDTO();
        CustomerDTO customer = (CustomerDTO) user;
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        String selectQuery = "SELECT * FROM users_table WHERE user_id = ?";
        
        try{
            
            pst = conn.prepareStatement(selectQuery);
            pst.setLong(1, userId);
            rs = pst.executeQuery();
            
            while(rs.next()){
                customer.setUserID(rs.getLong(1));
                customer.setEmail(rs.getString(2));
                customer.setUsername(rs.getString(3));
                customer.setPassword(rs.getString(4));
                customer.setName(rs.getString(5));
                customer.setPassportNo(rs.getString(6));
                customer.setStatus(rs.getString(7).charAt(0));
                customer.setAge(Integer.parseInt(rs.getString(8)));
                customer.setType(rs.getString(9));
            }
            
        }catch(SQLException e){
            e.printStackTrace();
        }catch(NumberFormatException e){
            e.printStackTrace();
        }finally{
            try{
                if(rs != null){
                    rs.close();
                }
                
                if(pst != null){
                    pst.close();
                }
                
                if(conn != null){
                    conn.close();
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return customer;
    }
    
    public static CustomerDTO getCustomerByUsername(String username){
        
        CustomerDTO customer = null;
        //UserDTO user = new UserDTO();
        
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        String selectQuery = "SELECT * FROM users_table WHERE username = ?";
        
        try{ 
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(selectQuery);
            pst.setString(1, username);
            rs = pst.executeQuery();
            
            
            
            if(rs.next()){
                
                customer = new CustomerDTO();
                
                customer.setUserID(rs.getLong(1));
                customer.setEmail(rs.getString(2));
                customer.setUsername(rs.getString(3));
                customer.setPassword(rs.getString(4));
                customer.setName(rs.getString(5));
                customer.setPassportNo(rs.getString(6));
                customer.setStatus(rs.getString(7).charAt(0));
                customer.setAge(Integer.parseInt(rs.getString(8).trim()));
                customer.setType(rs.getString(9));
            }
            
        }catch(SQLException e){
            e.printStackTrace();
        }catch(NumberFormatException e){
            e.printStackTrace();
        }finally{
            try{
                if(rs != null){
                    rs.close();
                }
                
                if(pst != null){
                    pst.close();
                }
                
                if(conn != null){
                    conn.close();
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return customer;
    }
    
    public static UserDTO userAuthentication(String username, String password){
        
        Connection conn = null;
        PreparedStatement pst = null;
        UserDTO user = null;
        
        String selectQuery = "SELECT * FROM users_table WHERE username = ? AND password = ?";
        
        try{
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(selectQuery);
            pst.setString(1, username);
            pst.setString(2, password);
            
            ResultSet rs = pst.executeQuery();
            
            if(rs.next()){                
                
                long userId = rs.getLong(1);
                String email = rs.getString("email").trim();
                String passportNo = rs.getString("passport_no").trim();
                char status = rs.getString("status").charAt(0);
                String type = rs.getString("user_type").trim();
                String ageString = rs.getString("age").trim();
                String usernameDB = rs.getString("username").trim();
                String passwordDB = rs.getString("password").trim();
                
                switch(type){
                    case "admin":
                        
                        // Administrator
                    
                        AdministratorDTO admin = new AdministratorDTO();
                        admin.setUserID(userId);
                        admin.setEmail(email);
                        admin.setUsername(usernameDB);
                        admin.setPassword(passwordDB);
                        admin.setStatus(status);
                        admin.setType(type);
                        
                        user = admin;
                        break;
                        
                    case "operator":
                        
                        // Operator

                        OperatorDTO operator = new OperatorDTO();
                        operator.setUserID(userId);
                        operator.setEmail(email);
                        operator.setUsername(usernameDB);
                        operator.setPassword(passwordDB);
                        operator.setName(email);
                        operator.setStatus(status);
                        operator.setType(type);

                        user = operator;
                        break;
                        
                    case "customer":
                        // Customer
                    
                        int age;
                        
                        if("-".equals(ageString)){
                            age = 0;
                        }else{
                            age = Integer.parseInt(ageString);
                        }
                        
                        
                        CustomerDTO customer = new CustomerDTO();
                        customer.setUserID(userId);
                        customer.setName(email);
                        customer.setEmail(email);
                        customer.setAge(age);
                        customer.setPassportNo(passportNo);
                        customer.setStatus(status);
                        customer.setUsername(usernameDB);
                        customer.setPassword(passwordDB);
                        customer.setType(type);

                        user = customer;
                        break;
                        
                    default:
                        System.out.println("Invalid user type");
                        
                }
                
                
            }
            
            if (user == null) {
                String msg = "User '" + username + "' was not found";
                throw new UserNotFoundException(msg);

            }
        }catch(SQLException e){
            e.printStackTrace();
        }catch(UserNotFoundException e){
            e.printStackTrace();
        }finally{
            try{
                if(pst != null){
                    pst.close();
                }
                
                if(conn != null){
                    conn.close();
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return user; // Returns null if not found
    }
    
    
    
}
