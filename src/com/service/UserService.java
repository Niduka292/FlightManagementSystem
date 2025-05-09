package com.service;

import com.DTO.AdministratorDTO;
import com.DTO.CustomerDTO;
import com.DTO.OperatorDTO;
import com.DTO.UserDTO;
import java.sql.*;
import com.util.JDBCUtil;

public class UserService {
    
   
    public static boolean createCustomer(UserDTO user){
        
        Connection conn = null;
        PreparedStatement pst = null;
        int rowsAffected = 0;
        CustomerDTO customer = null;
        boolean customerAdded = false;
        
        String insertQuery = "INSERT INTO users_table(user_id,email,username,"
                + "password,name,passport_no,status,age) values(?,?,?,?,?,?,?,?)";        
        
        if(user instanceof CustomerDTO){
            customer = (CustomerDTO) user;
        }else{
            System.out.println("Invalid user type provided.");
        }
        
        try{
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(insertQuery);
            pst.setString(1, customer.getUserID());
            pst.setString(2, customer.getEmail());
            pst.setString(3, customer.getUsername());
            pst.setString(4, customer.getPassword());
            pst.setString(5, customer.getName());
            pst.setString(6, customer.getPassportNo());
            pst.setString(7, String.valueOf(customer.getStatus()));
            pst.setString(8, String.valueOf(customer.getAge()));
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
        
        String insertQuery = "INSERT INTO users_table(user_id,email,username,"
                + "password,name,passport_no,status,age) values(?,?,?,?,?,?,?,?)";        
        
        if(user instanceof OperatorDTO){
            operator = (OperatorDTO) user;
        }else{
            System.out.println("Invalid user type provided.");
        }
        
        try{
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(insertQuery);
            pst.setString(1, operator.getUserID());
            pst.setString(2, operator.getEmail());
            pst.setString(3, operator.getUsername());
            pst.setString(4, operator.getPassword());
            pst.setString(5, operator.getName());
            pst.setString(6, "-");
            pst.setString(7, String.valueOf(operator.getStatus()));
            pst.setString(8, "-");
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
        
        String insertQuery = "INSERT INTO users_table(user_id,email,username,"
                + "password,name,passport_no,status,age) values(?,?,?,?,?,?,?,?)";        
        
        if(user instanceof AdministratorDTO){
            admin = (AdministratorDTO) user;
        }else{
            System.out.println("Invalid user type provided.");
        }
        
        try{
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(insertQuery);
            pst.setString(1, admin.getUserID());
            pst.setString(2, admin.getEmail());
            pst.setString(3, admin.getUsername());
            pst.setString(4, admin.getPassword());
            pst.setString(5, "-");
            pst.setString(6, "-");
            pst.setString(7, String.valueOf(admin.getStatus()));
            pst.setString(8, "-");
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
    
    
    
    public static CustomerDTO getCustomerById(String userId){
        
        UserDTO user = new UserDTO();
        CustomerDTO customer = (CustomerDTO) user;
        
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        String selectQuery = "SELECT * FROM users_table WHERE user_id = ?";
        
        try{
            
            
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(selectQuery);
            pst.setString(1, userId);
            rs = pst.executeQuery();
            
            while(rs.next()){
                customer.setUserID(rs.getString(1));
                customer.setEmail(rs.getString(2));
                customer.setUserID(rs.getString(3));
                customer.setPassword(rs.getString(4));
                customer.setName(rs.getString(5));
                customer.setPassportNo(rs.getString(6));
                customer.setStatus(rs.getString(7).charAt(0));
                customer.setAge(Integer.parseInt(rs.getString(8)));
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
        
        UserDTO user = new UserDTO();
        CustomerDTO customer = (CustomerDTO) user;
        
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        String selectQuery = "SELECT * FROM users_table WHERE username = ?";
        
        try{
            
            
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(selectQuery);
            pst.setString(1, username);
            rs = pst.executeQuery();
            
            while(rs.next()){
                customer.setUserID(rs.getString(1));
                customer.setEmail(rs.getString(2));
                customer.setUserID(rs.getString(3));
                customer.setPassword(rs.getString(4));
                customer.setName(rs.getString(5));
                customer.setPassportNo(rs.getString(6));
                customer.setStatus(rs.getString(7).charAt(0));
                customer.setAge(Integer.parseInt(rs.getString(8)));
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
        ResultSet rs = null;
        
        String selectQuery = "SELECT * FROM users_table WHERE username = ? AND password = ?";
        
        try{
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(selectQuery);
            pst.setString(1, username);
            pst.setString(2, password);
            
            rs = pst.executeQuery();
            
            while(rs.next()){                
                
                String userId = rs.getString(1);
                String email = rs.getString("email");
                String passportNo = rs.getString("passport_no");
                char status = rs.getString("status").charAt(0);
                int age = Integer.parseInt(rs.getString("age"));
                
                
                if(username.equals("admin") && password.equals("admin123")){
                    // Administrator
                    
                    AdministratorDTO admin = new AdministratorDTO();
                    admin.setUserID(userId);
                    admin.setEmail(email);
                    admin.setUsername(username);
                    admin.setPassword(password);
                    admin.setStatus(status);
                    
                    user = admin;
                    
                }else if("-".equals(age)){
                    // Operator
                    
                    OperatorDTO operator = new OperatorDTO();
                    operator.setUserID(userId);
                    operator.setEmail(email);
                    operator.setUsername(username);
                    operator.setPassword(password);
                    operator.setName(email);
                    operator.setStatus(status);
                    
                    user = operator;
                    
                }else{
                    // Customer
                    
                    CustomerDTO customer = new CustomerDTO();
                    customer.setUserID(userId);
                    customer.setName(email);
                    customer.setEmail(email);
                    customer.setAge(age);
                    customer.setPassportNo(passportNo);
                    customer.setStatus(status);
                    customer.setUsername(username);
                    customer.setPassword(password);
                    
                    user = customer;
                }
                
            }
        }catch(SQLException e){
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
        
        return user; // Returns null if not found
    }
    
    
    
}
