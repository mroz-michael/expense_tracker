package com.expense_tracker.db;

import com.expense_tracker.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryExecutor {
    //use prepared statements!

    public static void main(String[] args) {
        User test = getUser("admin", "users");
        System.out.println(test);
    }

    public static User getUser(String username, String dbName) {

        try {
            Connection mySql = DBConnector.connect("");
            String database = dbName.equals("") ? "users" : dbName;
            String getUserQuery = "SELECT id, username, password_hash, role from " + database + " where username = ? ";
            PreparedStatement fetchUser = mySql.prepareStatement(getUserQuery);
            fetchUser.setString(1, username);
            ResultSet res = fetchUser.executeQuery();
            String dbUsername = "";
            String dbPwHash = "";
            String dbRole = "";
            int id = -1;
            while (res.next()) {
                id = res.getInt(1);
                dbUsername = res.getString(2);
                dbPwHash = res.getString(3);
                dbRole = res.getString(4);
            }

            if (dbUsername.isEmpty() || dbPwHash.isEmpty() || dbRole.isEmpty()) {
                System.out.println(username + " not found in Database, returning null");
                return null;
            }

            return new User(id, dbUsername, dbPwHash, dbRole);

        } catch (SQLException | IOException e) {
            System.out.println("MySQL error: " + e.getMessage());
        }

        return null;
    }
    public static boolean updateUser(User updatedUser, String dbName) {
        //users can only update name and pw (hashed)
        try {
            Connection mySql = DBConnector.connect("");
            String newUsername = updatedUser.getUsername();
            String newPwHash = updatedUser.getPwHash();
            String database = dbName.equals("") ? "users" : dbName;
            String updateUserQuery = "update " + database + " " + "set username = ? , password_hash = ? where id= ?";
            PreparedStatement updateUser = mySql.prepareStatement(updateUserQuery);
            updateUser.setString(1, newUsername);
            updateUser.setString(2, newPwHash);
            updateUser.setInt(3, updatedUser.getId());
            int rowsEffected = updateUser.executeUpdate();
            return rowsEffected > 0;
        } catch (SQLException | IOException e) {
            System.out.println("Error connecting to DB");
            System.out.println(e.getMessage());
            return false;
        }
    }
}
