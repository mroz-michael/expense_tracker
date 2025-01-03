package com.expense_tracker.db;

import com.expense_tracker.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Date;

public class QueryExecutor {
    //use prepared statements!

    public static void main(String[] args) {
        User test = getUser(1, "users");
        System.out.println(test);
    }

    /**
     *
     * @param username username
     * @param pwHash hashed password
     * @param role owner | admin | user
     * @param dbName used to specify name of test db, if left blank will default to actual db
     * @return the User object created and returned by the DB
     */
    public static User createUser(String username, String pwHash, String role, String dbName) {

        try {
            Connection mySql = DBConnector.connect("");
            String database = dbName.equals("") ? "users" : dbName;
            String createUserQuery = "Insert into " + database + "(username, password_hash, role) " +
                    "VALUES(?, ?, ?);";
            //String[] generatedCols = {"ID", "username", "password_hash", "date_created", "role"};
            PreparedStatement newUser = mySql.prepareStatement(createUserQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            newUser.setString(1, username);
            newUser.setString(2, pwHash);
            newUser.setString(3, role);
            newUser.executeUpdate();
            ResultSet res = newUser.getGeneratedKeys();
            int id = -1;
            while (res.next()) {
                id = res.getInt(1);
            }

            User createdUser = getUser(id, dbName);

            return createdUser;

        } catch (SQLException | IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static User findUserByName(String username, String dbName) {
        try {
            Connection mySql = DBConnector.connect("");
            String database = dbName.equals("") ? "users" : dbName;
            String getUserQuery = "SELECT id, username, password_hash, date_created, role from " + database + " where username = ? ";
            PreparedStatement fetchUser = mySql.prepareStatement(getUserQuery);
            fetchUser.setString(1, username);
            ResultSet res = fetchUser.executeQuery();
            String dbUsername = "";
            String dbPwHash = "";
            String dbRole = "";
            Date dateJoined = new Date();
            int id = -1;

            while (res.next()) {
                id = res.getInt(1);
                dbUsername = res.getString(2);
                dbPwHash = res.getString(3);
                dateJoined = res.getDate(4);
                dbRole = res.getString(5);
            }

            if (dbUsername.isEmpty() || dbPwHash.isEmpty() || dbRole.isEmpty() || id == -1) {
                System.out.println("User with that Username not found in Database, returning null");
                return null;
            }

            return new User(id, dbUsername, dbPwHash, dateJoined, dbRole);

        } catch (SQLException | IOException e) {
            System.out.println("MySQL error: " + e.getMessage());
        }

        return null;
    }
    public static User getUser(int userID, String dbName) {

        try {
            Connection mySql = DBConnector.connect("");
            String database = dbName.equals("") ? "users" : dbName;
            String getUserQuery = "SELECT id, username, password_hash, date_created, role from " + database + " where id = ? ";
            PreparedStatement fetchUser = mySql.prepareStatement(getUserQuery);
            fetchUser.setInt(1, userID);
            ResultSet res = fetchUser.executeQuery();
            String dbUsername = "";
            String dbPwHash = "";
            String dbRole = "";
            Date dateJoined = new Date();
            int id = -1;

            while (res.next()) {
                id = res.getInt(1);
                dbUsername = res.getString(2);
                dbPwHash = res.getString(3);
                dateJoined = res.getDate(4);
                dbRole = res.getString(5);
            }

            if (dbUsername.isEmpty() || dbPwHash.isEmpty() || dbRole.isEmpty()) {
                System.out.println("User with that ID not found in Database, returning null");
                return null;
            }

            return new User(id, dbUsername, dbPwHash, dateJoined, dbRole);

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
