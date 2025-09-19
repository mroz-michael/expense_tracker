package com.expense_tracker.db;
import java.io.IOException;
import java.sql.*;

import com.expense_tracker.utils.ConfigLoader;

public class DBConnector {

    public static Connection connect() throws SQLException, IOException {
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            ConfigLoader config = ConfigLoader.getInstance();
            String url = config.getDBUrl();
            String username = config.getDBUser();
            String password = config.getDBPassword();
            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            System.out.println("Error Connecting to database" + e.getMessage());
            return null;
        }
    }
}