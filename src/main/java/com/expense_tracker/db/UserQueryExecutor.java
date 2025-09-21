package com.expense_tracker.db;

import com.expense_tracker.User;
import com.expense_tracker.utils.ConfigLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Date;

public class UserQueryExecutor {
    //use prepared statements!

    /*for testing purposes:
    public static void main(String[] args) {
        User test = getUser(1);
        System.out.println(test);
    }
    */

    private Connection mySql;
    private TableNameProvider tableProvider;

    public UserQueryExecutor(Connection mySql, TableNameProvider tableProvider) {
        this.mySql = mySql;
        this.tableProvider = tableProvider;
    }

    /**
     *
     * @param username username
     * @param pwHash hashed password
     * @param role owner | admin | user
     * @return the User object created and returned by the DB
     */
    public User createUser(String username, String pwHash, String role) {

        try {

            String tableName = tableProvider.provideUsersTable();
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

            User createdUser = getUser(id);
            return createdUser;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public User findUserByName(String username) {
        try {
            
            String tableName = tableProvider.provideUsersTable();
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

        } catch (SQLException e) {
            System.out.println("MySQL error: " + e.getMessage());
        }

        return null;
    }

    public User getUser(int userID) {

        try {
            
            String tableName = tableProvider.provideUsersTable();
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

        } catch (SQLException e) {
            System.out.println("MySQL error: " + e.getMessage());
        }

        return null;
    }
    public boolean updateUser(User updatedUser) {
        //users can only update name and pw (hashed)
        try {
            
            String newUsername = updatedUser.getUsername();
            String newPwHash = updatedUser.getPwHash();
            String tableName = tableProvider.provideUsersTable();
            String updateUserQuery = "update " + tableName + " " + "set username = ? , password_hash = ? where id= ?";
            PreparedStatement updateUser = mySql.prepareStatement(updateUserQuery);
            updateUser.setString(1, newUsername);
            updateUser.setString(2, newPwHash);
            updateUser.setInt(3, updatedUser.getId());
            int rowsEffected = updateUser.executeUpdate();
            return rowsEffected > 0;
        } catch (SQLException  e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean deleteUser(User toDelete) {

        if (toDelete == null) { return false; }

        try {

            String tableName = tableProvider.provideUsersTable();
            String deleteUserQuery = "delete from " + tableName + " where id = ? ";
            PreparedStatement removeUser = mySql.prepareStatement(deleteUserQuery);
            removeUser.setInt(1, toDelete.getId());
            int rowsEffected = removeUser.executeUpdate();

            return rowsEffected == 1;

        } catch (SQLException e) {
            System.out.println("Error attempting to delete user: " + e.getMessage());
            return false;
        }
    }

    /**
     * @return the number of users in the DB, or -1 if an error is encountered
     */
    public  int findNumUsers() {
        try {

            String tableName = tableProvider.provideUsersTable();
            String getNumUsersQuery = "select count(*) from " + tableName;
            PreparedStatement getNumUsers = mySql.prepareStatement(getNumUsersQuery);
            ResultSet res = getNumUsers.executeQuery();
            int numUsers = -1;
            while (res.next()) {
                numUsers = res.getInt(1);
            }
            return numUsers;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }
}