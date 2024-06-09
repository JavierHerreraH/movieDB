package com.example.movies;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(System.getenv("DATASOURCE_URL"));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Connection getConnection(String url) {
        try {
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
