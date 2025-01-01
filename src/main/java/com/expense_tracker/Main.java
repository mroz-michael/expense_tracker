package com.expense_tracker;

import com.expense_tracker.db.DBConnector;

import java.sql.*;

public class Main {
    public static void main( String[] args ) {

        try {
            Connection mySql = DBConnector.connect("");
            Statement stmt = mySql.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");
            //System.out.println("ID | Username | Password | Date Created |");
            while(rs.next()) {
                String id = rs.getString(1);
                String user = rs.getString(2);
                String email = rs.getString(3);
                String pw = rs.getString(4);
                String dateCreated = rs.getString(5);
                String role = rs.getString(6);
                System.out.println(id + " | " + user + " | " + pw + " | " + dateCreated);
            }

        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}