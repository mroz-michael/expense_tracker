package com.expense_tracker.db;

import com.expense_tracker.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class QueryExecutor {
    //use prepared statements!

    public static void main(String[] args) {
        User test = getUser("admin");
        System.out.println(test);
    }

    public static User getUser(String username) {

        try {
            Connection mySql = DBConnector.connect("");
            String getUserQuery = "SELECT username, password_hash, role from users where username = ? ";
            PreparedStatement fetchUser = mySql.prepareStatement(getUserQuery);
            fetchUser.setString(1, username);
            ResultSet res = fetchUser.executeQuery();
            String dbUsername = "";
            String dbPwHash = "";
            String dbRole = "";
            while (res.next()) {
                dbUsername = res.getString(1);
                dbPwHash = res.getString(2);
                dbRole = res.getString(3);
            }
            if (dbUsername.isEmpty() || dbPwHash.isEmpty() || dbRole.isEmpty()) {
                System.out.println("Error retrieving " + username + "from database. Aborting.");
                return null;
            }

            return new User(dbUsername, dbPwHash, dbRole);

        } catch (SQLException | IOException e) {
            System.out.println("MySQL error: " + e.getMessage());
        }
        return null;
    }
}
