package com.expense_tracker.db;

import com.expense_tracker.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Date;

//todo: extract sql query strings into UserQueries class
public class UserQueryExecutor {
    //use prepared statements!

    private final static String TEST_DB = "users_test";
    private final static String REG_DB = "users";

    public static void main(String[] args) {
        User test = getUser(1, false);
        System.out.println(test);
    }

    public static String getTableName(boolean isTest) {
        return isTest ? TEST_DB : REG_DB;
    }

    /**
     *
     * @param username username
     * @param pwHash hashed password
     * @param role owner | admin | user
     * @param isTest used to specify name of test db, if left blank will default to actual db
     * @return the User object created and returned by the DB
     */
    public static User createUser(String username, String pwHash, String role, boolean isTest) {

        try {
            Connection mySql = DBConnector.connect();
            String tableName = getTableName(isTest);
            String createUserQuery = "Insert into " + tableName + "(username, password_hash, role) " +
                    "VALUES(?, ?, ?);";
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

            User createdUser = getUser(id, isTest);
            return createdUser;

        } catch (SQLException | IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static User findUserByName(String username, boolean isTest) {
        try {
            Connection mySql = DBConnector.connect();
            String tableName = getTableName(isTest);
            String getUserQuery = "SELECT id, username, password_hash, date_created, role from " + tableName + " where username = ? ";
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
                return null;
            }

            return new User(id, dbUsername, dbPwHash, dateJoined, dbRole);

        } catch (SQLException | IOException e) {
            System.out.println("MySQL error: " + e.getMessage());
        }

        return null;
    }
    public static User getUser(int userID, boolean isTest) {

        try {
            Connection mySql = DBConnector.connect();
            String tableName = getTableName(isTest);
            String getUserQuery = "SELECT id, username, password_hash, date_created, role from " + tableName + " where id = ? ";
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
                return null;
            }

            return new User(id, dbUsername, dbPwHash, dateJoined, dbRole);

        } catch (SQLException | IOException e) {
            System.out.println("MySQL error: " + e.getMessage());
        }

        return null;
    }
    public static boolean updateUser(User updatedUser, boolean isTest) {
        //users can only update name and pw (hashed)
        try {
            Connection mySql = DBConnector.connect();
            String newUsername = updatedUser.getUsername();
            String newPwHash = updatedUser.getPwHash();
            String tableName = getTableName(isTest);
            String updateUserQuery = "update " + tableName + " " + "set username = ? , password_hash = ? where id= ?";
            PreparedStatement updateUser = mySql.prepareStatement(updateUserQuery);
            updateUser.setString(1, newUsername);
            updateUser.setString(2, newPwHash);
            updateUser.setInt(3, updatedUser.getId());
            int rowsEffected = updateUser.executeUpdate();
            return rowsEffected > 0;
        } catch (SQLException | IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static boolean deleteUser(User toDelete, boolean isTest) {

        if (toDelete == null) { return false; }

        try {

            Connection mySql = DBConnector.connect();
            String tableName = getTableName(isTest);
            String deleteUserQuery = "delete from " + tableName + " where id = ? ";
            PreparedStatement removeUser = mySql.prepareStatement(deleteUserQuery);
            removeUser.setInt(1, toDelete.getId());
            int rowsEffected = removeUser.executeUpdate();

            return rowsEffected == 1;

        } catch (SQLException | IOException e) {
            System.out.println("Error attempting to delete user: " + e.getMessage());
            return false;
        }
    }

    /**
     * @param isTest flag to determine whether to use regular or test table in db
     * @return the number of users in the DB, or -1 if an error is encountered
     */
    public static int findNumUsers(boolean isTest) {
        try {
            Connection mySql = DBConnector.connect();
            String tableName = getTableName(isTest);
            String getNumUsersQuery = "select count(*) from " + tableName;
            PreparedStatement getNumUsers = mySql.prepareStatement(getNumUsersQuery);
            ResultSet res = getNumUsers.executeQuery();
            int numUsers = -1;
            while (res.next()) {
                numUsers = res.getInt(1);
            }
            return numUsers;
        } catch (SQLException | IOException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }
}