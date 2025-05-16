package com.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

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
            //System.out.println("Connection established successfully");
        }else{
            System.out.println("Connection failed. Recheck username and password.");
        }
        
        return conn;
    } 
    
    public static ZonedDateTime convertTimeStampToZoneDate(String continent, String city, Timestamp timestamp) {

        if(timestamp == null || continent == null || city == null){
            throw new IllegalArgumentException("Continent, city, and timestamp must not be null.");
        }

        Instant instant = timestamp.toInstant();
        String zoneId = null;

        // Convert continent and city to lowercase for case-insensitive comparison
        String cont = continent.toLowerCase();
        String ct = city.toLowerCase();

        if (cont.equals("asia")) {
            if(ct.equals("tokyo")){
                zoneId = "Asia/Tokyo";
            }else if(ct.equals("shanghai")){
                zoneId = "Asia/Shanghai";
            }else if(ct.equals("seoul")) {
                zoneId = "Asia/Seoul";
            }else if(ct.equals("dubai")) {
                zoneId = "Asia/Dubai";
            }else if(ct.equals("jakarta")) {
                zoneId = "Asia/Jakarta";
            }else{
                zoneId = "Asia/Kolkata"; // Default value for Asia
            }
        }else if(cont.equals("europe")) {
            if(ct.equals("london")) {
                zoneId = "Europe/London";
            }else if(ct.equals("paris")) {
                zoneId = "Europe/Paris";
            }else if(ct.equals("madrid")) {
                zoneId = "Europe/Madrid";
            }else if(ct.equals("rome")) {
                zoneId = "Europe/Rome";
            }else if(ct.equals("moscow")) {
                zoneId = "Europe/Moscow";
            }else{
                zoneId = "Europe/Berlin"; // Default value for Europe
            }
        }else if(cont.equals("america")) {
            if(ct.equals("chicago") || ct.equals("houston")) {
                zoneId = "America/Chicago";
            }else if(ct.equals("los angeles")) {
                zoneId = "America/Los_Angeles";
            }else if(ct.equals("phoenix")) {
                zoneId = "America/Phoenix";
            }else if(ct.equals("sao paulo")) {
                zoneId = "America/Sao_Paulo";
            }else if(ct.equals("buenos aires")) {
                zoneId = "America/Buenos_Aires";
            }else{
                zoneId = "America/New_York";
            }
        }else if(cont.equals("africa")){
            if(ct.equals("cairo")) {
                zoneId = "Africa/Cairo";
            }else if(ct.equals("nairobi")) {
                zoneId = "Africa/Nairobi";
            }else if(ct.equals("lagos")) {
                zoneId = "Africa/Lagos";
            }else if(ct.equals("johannesburg")) {
                zoneId = "Africa/Johannesburg";
            }
        }else if (cont.equals("australia") || cont.equals("oceania")) {
            if(ct.equals("sydney")) {
                zoneId = "Australia/Sydney";
            }else if(ct.equals("perth")) {
                zoneId = "Australia/Perth";
            }else if(ct.equals("auckland")) {
                zoneId = "Pacific/Auckland";
            }
        }

        if(zoneId == null){
            zoneId = "UTC";
        }

        return instant.atZone(ZoneId.of(zoneId));
        
    }
    
    
    public static String convertZoneDateTimeToString(ZonedDateTime zdt){
        
        String isoString = zdt.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
        return isoString;   
    }
    
    public static ZonedDateTime convertStringToZonedDateTime(String dateString){
        
        dateString = dateString.trim();
        ZonedDateTime parsedZdt = ZonedDateTime.parse(dateString, DateTimeFormatter.ISO_ZONED_DATE_TIME);
        return parsedZdt;
    }
    
}
