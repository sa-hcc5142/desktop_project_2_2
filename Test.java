package com.example.login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Test {
    public static void main(String[] args) {
        System.out.println(System.getProperty("java.class.path"));
        String url = "jdbc:mysql://localhost:3306/mentalhealth";
        String user = "root";  // Replace with your username
        String password = "1234";  // Replace with your password

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connection successful!");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
