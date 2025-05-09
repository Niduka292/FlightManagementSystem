package com.service;

import java.sql.*;
import com.util.JDBCUtil;
import com.DTO.AircraftDTO;

public class AircraftService {
    
    public static boolean registerAircraft(AircraftDTO aircraft){
        
        Connection conn = null;
        PreparedStatement pst = null;
        boolean aircraftRegSuccess = false;
        
        String insertQuery = "INSERT INTO aircrafts_table(aircraft_id,model,category,first_class_seats,"
                + "business_class_seats,economy_class_seats) VALUES (?,?,?,?,?,?)";
        
        try{
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(insertQuery);
            
            pst.setString(1, aircraft.getAircraftID());
            pst.setString(2, aircraft.getModel());
            pst.setString(3, aircraft.getCategory());
            pst.setInt(4, aircraft.getNoOfFirstClassSeats());
            pst.setInt(5, aircraft.getNoOfBusinessSeats());
            pst.setInt(6, aircraft.getNoOfEconomySeats());
            
            int rowsAffected = pst.executeUpdate();
            
            if(rowsAffected >  0){
                System.out.println("Aircraft registered successfully.");
                aircraftRegSuccess = true;
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
        
        return aircraftRegSuccess;
    }
    
    public static AircraftDTO getAircraftById(String aircraftId){
        
        Connection conn = null;
        PreparedStatement pst = null;
        AircraftDTO aircraft = new AircraftDTO();
        
        String selectQuery = "SELECT * FROM aircrafts_table WHERE aircraft_id = ?";
        
        try{
            conn = JDBCUtil.getConnection();
            pst = conn.prepareStatement(selectQuery);
            
            pst.setString(1, aircraftId);
            
            ResultSet rs = pst.executeQuery();
            
            while(rs.next()){
                aircraft.setAircraftID(aircraftId);
                aircraft.setModel(rs.getString(2));
                aircraft.setCategory(rs.getString(3));
                aircraft.setNoOfFirstClassSeats(rs.getInt(4));
                aircraft.setNoOfBusinessSeats(rs.getInt(5));
                aircraft.setNoOfEconomySeats(rs.getInt(6));
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
        return aircraft;
    }
}
