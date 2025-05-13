package com.service;

import com.DTO.AirportDTO;
import java.sql.*;
import com.util.JDBCUtil;

public class AirportService {
   
    public static boolean addAirport(AirportDTO airport){
        
        boolean airportAdded = false;
        Connection conn = null;
        PreparedStatement pst = null;
        
        String insertQuery = "INSERT INTO airports_table(airport_code,airport_name,city, country, continent)"
                + "VALUES (?,?,?,?,?)";
        
        try{
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(insertQuery);
            pst.setString(1, airport.getAirportCode());
            pst.setString(2, airport.getAirportName());
            pst.setString(3, airport.getCity());
            pst.setString(4, airport.getCountry());
            pst.setString(5, airport.getContinent());
            
            int rowsAffected = pst.executeUpdate();
            
            if(rowsAffected > 0){
                System.out.println("Airport added successfully.");
                airportAdded = true;
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
        
        return airportAdded;
    }
    
    public static AirportDTO getAirportById(String airportId){
        
        Connection conn = null;
        PreparedStatement pst = null;
        AirportDTO airport = null;
        ResultSet rs = null;
        
        String selectQuery = "SELECT * FROM airports_table where airport_id = ?";
        
        try{
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(selectQuery);
            pst.setString(1, airportId);
            rs = pst.executeQuery();
            
            if(rs.next()){
                airport = new AirportDTO();
                airport.setAirportCode(rs.getString("airport_code"));
                airport.setAirportName(rs.getString("airport_name"));
                airport.setCity(rs.getString("city"));
                airport.setCountry(rs.getString("country"));
                airport.setContinent(rs.getString("continent"));
                
            }else{
                System.out.println("Airport with entered ID do not exist.");
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
        
        return airport;
        
    }
    
}
