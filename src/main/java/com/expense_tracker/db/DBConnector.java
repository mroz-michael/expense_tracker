package com.expense_tracker.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DBConnector {

    public static Connection connect(String filepath) throws SQLException, IOException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //add url/dbuser/dbpw/env in ./resources/config.properties
            Properties properties = new Properties();
            String configFile = filepath == "" ? "src\\main\\java\\com\\expense_tracker\\resources\\config.properties" : filepath;
            FileInputStream fileInput = new FileInputStream(configFile);
            properties.load(fileInput);
            String url = properties.getProperty("db.url");
            String username = properties.getProperty("db.username");
            String password = properties.getProperty("db.password");

            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
