package com.service;

import com.DTO.AdministratorDTO;
import com.DTO.CustomerDTO;
import com.DTO.OperatorDTO;
import com.DTO.UserDTO;
import java.sql.*;
import com.util.JDBCUtil;
import com.util.UserNotFoundException;
import java.util.ArrayList;
import java.util.List;

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
            
            long userId = getUserIdByDetails(user);
            user.setUserID(userId);
            
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
            
            long userId = getUserIdByDetails(user);
            user.setUserID(userId);
            
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
            
            long userId = getUserIdByDetails(user);
            user.setUserID(userId);
            
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
    
    
    
    public static UserDTO getUserById(long enteredUserId, Connection conn){
        
        UserDTO user = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        String selectQuery = "SELECT * FROM users_table WHERE user_id = ?";
        
        try{
            
            pst = conn.prepareStatement(selectQuery);
            pst.setLong(1, enteredUserId);
            rs = pst.executeQuery();
            
            if(rs.next()){
                String role = rs.getString("user_type").trim();
                
                Long userId = rs.getLong("user_id");
                String email = rs.getString("email").trim();
                String username = rs.getString("username").trim();
                String password = rs.getString("password").trim();
                char status = rs.getString("status").charAt(0);
                
                switch(role){
                    case "customer":
                        CustomerDTO customer = new CustomerDTO();
                        
                        customer.setUserID(userId);
                        customer.setEmail(email);
                        customer.setUsername(username);
                        customer.setPassword(password);
                        customer.setName(rs.getString("name"));
                        customer.setPassportNo(rs.getString("passport_no"));
                        customer.setStatus(status);
                        customer.setAge(Integer.parseInt(rs.getString("age").trim()));
                        customer.setType(role);
                        user = customer;
                        
                        break;
                        
                    case "operator":
                        OperatorDTO operator = new OperatorDTO();
                        
                        operator.setUserID(userId);
                        operator.setName(rs.getString("name"));
                        operator.setEmail(email);
                        operator.setStatus(status);
                        operator.setUsername(username);
                        operator.setPassword(password);
                        operator.setType(role);
                        user = operator;
                        
                        break;
                        
                    case "admin":
                        AdministratorDTO admin = new AdministratorDTO();
                        
                        admin.setUserID(userId);
                        admin.setEmail(email);
                        admin.setStatus(status);
                        admin.setType(role);
                        admin.setUsername(username);
                        admin.setPassword(password);
                        user = admin;
                        
                        break;
                        
                    default:
                        System.out.println("Invalid user type");
                        
                }
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
                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return user;
    }
    
    public static UserDTO getUserByUsername(String enteredUsername){
        
        UserDTO user = null;        
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        String selectQuery = "SELECT * FROM users_table WHERE username = ?";
        
        try{ 
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(selectQuery);
            pst.setString(1, enteredUsername.trim());
            rs = pst.executeQuery();
            
            if(rs.next()){
                
                
                String role = rs.getString("user_type").trim();
                
                Long userId = rs.getLong("user_id");
                String email = rs.getString("email").trim();
                String username = rs.getString("username").trim();
                String password = rs.getString("password").trim();
                char status = rs.getString("status").charAt(0);
                
                switch(role){
                    case "customer":
                        CustomerDTO customer = new CustomerDTO();
                        
                        customer.setUserID(userId);
                        customer.setEmail(email);
                        customer.setUsername(username);
                        customer.setPassword(password);
                        customer.setName(rs.getString("name"));
                        customer.setPassportNo(rs.getString("passport_no"));
                        customer.setStatus(status);
                        customer.setAge(Integer.parseInt(rs.getString("age").trim()));
                        customer.setType(role);
                        user = customer;
                        
                        break;
                        
                    case "operator":
                        OperatorDTO operator = new OperatorDTO();
                        
                        operator.setUserID(userId);
                        operator.setName(rs.getString("name"));
                        operator.setEmail(email);
                        operator.setStatus(status);
                        operator.setUsername(username);
                        operator.setPassword(password);
                        operator.setType(role);
                        user = operator;
                        
                        break;
                        
                    case "admin":
                        AdministratorDTO admin = new AdministratorDTO();
                        
                        admin.setUserID(userId);
                        admin.setEmail(email);
                        admin.setStatus(status);
                        admin.setType(role);
                        admin.setUsername(username);
                        admin.setPassword(password);
                        user = admin;
                        
                        break;
                        
                    default:
                        System.out.println("Invalid user type");
                        
                }
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
        
        return user;
    }
    
    public static UserDTO getUserByEmail(String enteredEmail){
        
        UserDTO user = null;
        enteredEmail = enteredEmail.trim();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        String selectQuery = "SELECT * FROM users_table WHERE email = ?";
        
        try{ 
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(selectQuery);
            pst.setString(1, enteredEmail);
            rs = pst.executeQuery();
            
            if(rs.next()){
                String role = rs.getString("user_type").trim();
                
                Long userId = rs.getLong("user_id");
                String email = rs.getString("email").trim();
                String username = rs.getString("username").trim();
                String password = rs.getString("password").trim();
                char status = rs.getString("status").charAt(0);
                
                switch(role){
                    case "customer":
                        CustomerDTO customer = new CustomerDTO();
                        
                        customer.setUserID(userId);
                        customer.setEmail(email);
                        customer.setUsername(username);
                        customer.setPassword(password);
                        customer.setName(rs.getString("name"));
                        customer.setPassportNo(rs.getString("passport_no"));
                        customer.setStatus(status);
                        customer.setAge(Integer.parseInt(rs.getString("age").trim()));
                        customer.setType(role);
                        user = customer;
                        
                        break;
                        
                    case "operator":
                        OperatorDTO operator = new OperatorDTO();
                        
                        operator.setUserID(userId);
                        operator.setName(rs.getString("name"));
                        operator.setEmail(email);
                        operator.setStatus(status);
                        operator.setUsername(username);
                        operator.setPassword(password);
                        operator.setType(role);
                        user = operator;
                        
                        break;
                        
                    case "admin":
                        AdministratorDTO admin = new AdministratorDTO();
                        
                        admin.setUserID(userId);
                        admin.setEmail(email);
                        admin.setStatus(status);
                        admin.setType(role);
                        admin.setUsername(username);
                        admin.setPassword(password);
                        user = admin;
                        
                        break;
                        
                    default:
                        System.out.println("Invalid user type");
                        
                }
                
                
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
        
        return user;
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
    
    public static boolean deactivateUser(Long userId){
        
        Connection conn = null;
        PreparedStatement pst = null;
        boolean userDeactivated = false;
        
        String updateQuery = "UPDATE users_table SET status = 'I' WHERE user_id = ?";
        
        try{
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(updateQuery);
            pst.setLong(1, userId);

            int rowsUpdated = pst.executeUpdate();
            
            if(rowsUpdated > 0){
                System.out.println("Details updated successfully.");
                userDeactivated = true;
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
        
        return userDeactivated;
    }
    
    public static boolean activateUser(Long userId){
        
        Connection conn = null;
        PreparedStatement pst = null;
        boolean userDeactivated = false;
        
        String updateQuery = "UPDATE users_table SET status = 'A' WHERE user_id = ?";
        
        try{
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(updateQuery);
            pst.setLong(1, userId);

            int rowsUpdated = pst.executeUpdate();
            
            if(rowsUpdated > 0){
                System.out.println("Details updated successfully.");
                userDeactivated = true;
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
        
        return userDeactivated;
    }
    
    public static Long getUserIdByDetails(UserDTO user){
        
        Connection conn = null;
        PreparedStatement pst = null;
        Long userId = null;
        ResultSet rs = null;
        
        String selectQuery = "SELECT user_id FROM users_table WHERE username = ? AND email = ?";
        
        try{
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(selectQuery);
            pst.setString(1, user.getUsername());
            pst.setString(2, user.getEmail());
            rs = pst.executeQuery();
            if(rs.next()){
                userId = rs.getLong("user_id");
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
        
        return userId;
    }
    
    public static List<CustomerDTO> getCustomersByFlight(Long flightId, Connection conn){
        
        PreparedStatement pst = null;
        List<CustomerDTO> customers = new ArrayList<>();
        ResultSet rs = null;
        String[] flightCustomers = new String[0];
        
        String selectQuery = "SELECT customers_list FROM flights_table WHERE flight_id = ?";
        
        try{
            
            pst = conn.prepareStatement(selectQuery);
            pst.setLong(1, flightId);
            rs = pst.executeQuery();
            
            if(rs.next()){
                Array sqlArray = rs.getArray("customers_list");
                
                if(sqlArray != null){
                    flightCustomers = (String[]) sqlArray.getArray();
                    
                    for(String idStr : flightCustomers){
                        if(idStr != null && !idStr.trim().isEmpty()){
                            Long customerId = Long.parseLong(idStr);
                            UserDTO user = UserService.getUserById(customerId,conn);
                            CustomerDTO customer = null;
                            if(user.getType().equals("customer")){
                                customer = (CustomerDTO) user;
                            }
                            
                            if(customer != null){
                                customers.add(customer);
                            }else{
                                System.out.println("No customer found with ID: "+customerId);
                            }
                        }
                    }
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
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return customers;
    }
    
    public static void updateEmail(Long customerId, String newEmail){
        
        Connection conn = null;
        PreparedStatement pst = null;
        
        String updateQuery = "UPDATE users_table SET email = ? WHERE user_id = ?";
        
        try{
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(updateQuery);
            pst.setString(1, newEmail);
            pst.setLong(2, customerId);
            int rowsUpdated = pst.executeUpdate();
            
            if(rowsUpdated > 0){
                System.out.println("Details updated successfully");
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
    }
    
    public static void updateUsername(String newUsername, Long customerId){
        
        Connection conn = null;
        PreparedStatement pst = null;
        
        String updateQuery = "UPDATE users_table SET username = ? WHERE user_id = ?";
        
        try{
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(updateQuery);
            pst.setString(1, newUsername);
            pst.setLong(2, customerId);
            int rowsUpdated = pst.executeUpdate();
            
            if(rowsUpdated > 0){
                System.out.println("Details updated successfully");
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
    }
    
    public static void updateCustomerAge(int newAge, Long customerId){
        
        Connection conn = null;
        PreparedStatement pst = null;
        
        String updateQuery = "UPDATE users_table SET age = ? WHERE user_id = ?";
        
        try{
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(updateQuery);
            pst.setString(1, String.valueOf(newAge));
            pst.setLong(2, customerId);
            int rowsUpdated = pst.executeUpdate();
            
            if(rowsUpdated > 0){
                System.out.println("Details updated successfully");
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
    }
    
    public static void updateCustomerName(String newName, Long customerId){
        
        Connection conn = null;
        PreparedStatement pst = null;
        
        String updateQuery = "UPDATE users_table SET name = ? WHERE user_id = ?";
        
        try{
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(updateQuery);
            pst.setString(1, newName);
            pst.setLong(2, customerId);
            int rowsUpdated = pst.executeUpdate();
            
            if(rowsUpdated > 0){
                System.out.println("Details updated successfully");
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
    }
    
    public static void updateCustomerPassportNo(String newPassportNo, Long customerId){
        
        Connection conn = null;
        PreparedStatement pst = null;
        
        String updateQuery = "UPDATE users_table SET passport_no = ? WHERE user_id = ?";
        
        try{
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(updateQuery);
            pst.setString(1, newPassportNo);
            pst.setLong(2, customerId);
            int rowsUpdated = pst.executeUpdate();
            
            if(rowsUpdated > 0){
                System.out.println("Details updated successfully");
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
    }
    
}
