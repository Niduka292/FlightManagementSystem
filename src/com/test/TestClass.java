package com.test;

import com.util.JDBCUtil;
import java.sql.Connection;

public class TestClass {

    public static void main(String[] args) {

        Connection conn = JDBCUtil.getConnection();

    }
}
