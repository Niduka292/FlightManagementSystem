package com.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class JDBCUtil {
    
    private static final String DB_DRIVER = "org.postgresql.Driver";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/FlightManagementSystem";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "highway55";
    
    public static Connection getConnection(){
        
        Connection conn = null;
        try{
            Class.forName(DB_DRIVER);
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            
        }catch(ClassNotFoundException e){
            System.out.println("Driver class not found.\n"+e.getMessage());
        }catch(SQLException e){
            e.printStackTrace();
        }
        
        if(conn != null){
            System.out.println("Connection established successfully");
        }else{
            System.out.println("Connection failed. Recheck username and password.");
        }
        
        return conn;
    } 
    
    
}
