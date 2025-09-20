package com.expense_tracker.db;
import java.io.IOException;
import java.sql.*;

import com.expense_tracker.utils.ConfigLoader;

public class TestDBConnector {

    //Must have a test_config.properties file in resources dir with DBUrl, DBUser, DBPassword entries
    private static final String PATH_TO_TEST_PROPERTIES_FILE = "src\\main\\java\\com\\expense_tracker\\resources\\test_config.properties";

    public static Connection connect() throws SQLException, IOException {
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            
            ConfigLoader config = ConfigLoader.createTestInstance(PATH_TO_TEST_PROPERTIES_FILE);
            String url = config.getDBUrl();
            String username = config.getDBUser();
            String password = config.getDBPassword();
            System.out.println("Url: " + url + " username: " + username + " pw: " + password);
            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            System.out.println("Error Connecting to database" + e.getMessage());
            return null;
        }
    }
}