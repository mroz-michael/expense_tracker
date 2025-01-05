package com.expense_tracker.db;

import com.expense_tracker.Transaction;
import com.expense_tracker.User; //pass user to check user.id || user.type == owner/admin

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionQueryExecutor {

    private static final String TEST_DB = "transactions_test";
    private static final String REG_DB = "transactions";

    private static String getTableName(boolean isTest) {
        return isTest ? TEST_DB : REG_DB;
    }

    public static Transaction createTransaction(double amount, String description, String category, int userId, boolean isTest) {
        Date date = new Date(System.currentTimeMillis());
        return createTransaction(amount, description, category, userId, date, isTest);
    }
    public static Transaction createTransaction(
            double amount,
            String description,
            String category,
            int userId,
            Date date,
            boolean isTest
        ) {
            try {
                Connection mySql = DBConnector.connect("");
                String tableName = getTableName(isTest);
                String createTransactionQuery = "Insert into " + tableName +
                        "(amount, description, category, user_id, date) " +
                        "VALUES(?, ?, ?, ?, ?);";
                PreparedStatement newTransaction = mySql.prepareStatement(createTransactionQuery, PreparedStatement.RETURN_GENERATED_KEYS);
                newTransaction.setDouble(1, amount);
                newTransaction.setString(2, description);
                newTransaction.setString(3, category);
                newTransaction.setInt(4, userId);
                newTransaction.setDate(5, date);
                newTransaction.executeUpdate();
                ResultSet res = newTransaction.getGeneratedKeys();
                int id = -1;
                while (res.next()) {
                    id = res.getInt(1);
                }

                Transaction createdTransaction = getTransaction(id, isTest);

                return createdTransaction;
            }
            catch (SQLException | IOException e) {
                System.out.println(e.getMessage());
                return null;
            }
    }

    public static Transaction getTransaction(int id, boolean isTest) {

        try {
            Connection mySql = DBConnector.connect("");
            String tableName = getTableName(isTest);
            String getTransactionQuery = "SELECT id, description, amount, date, user_id, category from "
                    + tableName + " where id = ? ";
            PreparedStatement fetchTransaction = mySql.prepareStatement(getTransactionQuery);
            fetchTransaction.setInt(1, id);
            ResultSet res = fetchTransaction.executeQuery();

            int dbId = -1;
            String dbDescription = "";
            double dbAmount = 0.00;
            Date dbDate = null;
            int dbUserId = -1;
            String dbCategory = "";

            while (res.next()) {
                dbId = res.getInt(1);
                dbDescription = res.getString(2);
                dbAmount = res.getDouble(3);
                dbDate = res.getDate(4);
                dbUserId = res.getInt(5);
                dbCategory = res.getString(6);
            }

            if (dbId == -1) {
                System.out.println("Database query returned unexpected result, returning null");
                return null;
            }

            return new Transaction(dbId, dbAmount, dbDescription, dbCategory, dbUserId, dbDate);

        } catch (SQLException | IOException e) {
            System.out.println("MySQL error: " + e.getMessage());
            return null;
        }
    }
    public static List<Transaction> getAllTransactions(User user, boolean isTest) {
        List<Transaction> transactions = new ArrayList<>();

        if (user == null) {return transactions;}

        try {
            Connection mySql = DBConnector.connect("");
            String tableName = getTableName(isTest);
            int userId = user.getId();
            String getTransactionQuery = "SELECT id, description, amount, date, user_id, category from "
                    + tableName + " where user_id = ? ";
            PreparedStatement fetchTransaction = mySql.prepareStatement(getTransactionQuery);
            fetchTransaction.setInt(1, userId);
            ResultSet res = fetchTransaction.executeQuery();

            int dbId = -1;
            String dbDescription = "";
            double dbAmount = 0.00;
            Date dbDate = null;
            int dbUserId = -1;
            String dbCategory = "";

            while (res.next()) {
                dbId = res.getInt(1);
                dbDescription = res.getString(2);
                dbAmount = res.getDouble(3);
                dbDate = res.getDate(4);
                dbUserId = res.getInt(5);
                dbCategory = res.getString(6);
                Transaction t = new Transaction(dbId, dbAmount, dbDescription, dbCategory, dbUserId, dbDate);
                transactions.add(t);
            }

        } catch (SQLException | IOException e) {
            System.out.println("MySQL error: " + e.getMessage());
        }
        return transactions;
    }

    public static boolean updateTransaction(Transaction updatedTransaction, boolean isTest) {
        try {
            Connection mySql = DBConnector.connect("");
            String tableName = getTableName(isTest);
            String updateTransactionQuery = "update " + tableName + " " + "set amount = ?" +
                    ", description = ?, category = ? where id= ?";
            PreparedStatement updateTransaction = mySql.prepareStatement(updateTransactionQuery);
            updateTransaction.setDouble(1, updatedTransaction.getAmount());
            updateTransaction.setString(2, updatedTransaction.getDescription());
            updateTransaction.setString(3, updatedTransaction.getCategory());
            updateTransaction.setInt(4, updatedTransaction.getId());
            int rowsEffected = updateTransaction.executeUpdate();
            return rowsEffected > 0;
        } catch (SQLException | IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static boolean deleteTransaction(Transaction toDelete, boolean isTest) {

        try {
            Connection mySql = DBConnector.connect("");
            String tableName = getTableName(isTest);
            String deleteUserQuery = "delete from " + tableName + " where id = ? ";
            PreparedStatement removeUser = mySql.prepareStatement(deleteUserQuery);
            removeUser.setInt(1, toDelete.getId());
            int rowsEffected = removeUser.executeUpdate();

            return rowsEffected == 1;

        } catch (SQLException | IOException e) {
            System.out.println("Error attempting to delete transaction: " + e.getMessage());
        }

        return false;
    }

}
