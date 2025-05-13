package com.test;

import com.util.JDBCUtil;
import java.sql.*;

public class TestClass {

    public static void main(String[] args) {
        
        Connection conn = JDBCUtil.getConnection();
    }
    
}
