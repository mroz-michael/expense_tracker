package com.expense_tracker.db;

import com.expense_tracker.Transaction;
import com.expense_tracker.User; //pass user to check user.id || user.type == owner/admin

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
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
                String createTransactionQuery = TransactionQueries.create(tableName);
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
            String getTransactionQuery = TransactionQueries.getOne(tableName);
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
                System.out.println("Could not find Transaction with given id, returning null");
                return null;
            }

            return new Transaction(dbId, dbAmount, dbDescription, dbCategory, dbUserId, dbDate);

        } catch (SQLException | IOException e) {
            System.out.println("MySQL error: " + e.getMessage());
            return null;
        }
    }
    public static List<Transaction> getAllTransactions(int userId, boolean isTest) {
        List<Transaction> transactions = new ArrayList<>();

        try {
            Connection mySql = DBConnector.connect("");
            String tableName = getTableName(isTest);
            String getTransactionQuery = TransactionQueries.getAll(tableName);
            PreparedStatement fetchTransaction = mySql.prepareStatement(getTransactionQuery);
            fetchTransaction.setInt(1, userId);

            ResultSet res = fetchTransaction.executeQuery();
            populateList(transactions, res);

        } catch (SQLException | IOException e) {
            System.out.println("MySQL error: " + e.getMessage());
        }
        return transactions;
    }

    public static boolean updateTransaction(Transaction updatedTransaction, boolean isTest) {
        try {
            Connection mySql = DBConnector.connect("");
            String tableName = getTableName(isTest);
            String updateTransactionQuery = TransactionQueries.update(tableName);

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
            String deleteUserQuery = TransactionQueries.delete(tableName);
            PreparedStatement removeUser = mySql.prepareStatement(deleteUserQuery);
            removeUser.setInt(1, toDelete.getId());
            int rowsEffected = removeUser.executeUpdate();

            return rowsEffected == 1;

        } catch (SQLException | IOException e) {
            System.out.println("Error attempting to delete transaction: " + e.getMessage());
        }

        return false;
    }

    public static List<Transaction> getTransactionsByAmount(int userId, double min, double max, boolean isTest) {
        List<Transaction> transactions = new ArrayList<>();

        try {

            Connection mySql = DBConnector.connect("");
            String tableName = getTableName(isTest);
            String getTransactionQuery = TransactionQueries.getByAmount(tableName);
            PreparedStatement fetchTransaction = mySql.prepareStatement(getTransactionQuery);
            fetchTransaction.setInt(1, userId);
            fetchTransaction.setDouble(2, min);
            fetchTransaction.setDouble(3, max);

            ResultSet res = fetchTransaction.executeQuery();
            populateList(transactions, res);

        } catch (SQLException | IOException e) {
            System.out.println("MySQL error: " + e.getMessage());
        }
        return transactions;
    }

    public static List<Transaction> getTransactionsByCategory(int userId, String category, boolean isTest) {
        List<Transaction> transactions = new ArrayList<>();

        try {

            Connection mySql = DBConnector.connect("");
            String tableName = getTableName(isTest);
            String getTransactionQuery = TransactionQueries.getByCategory(tableName);
            PreparedStatement fetchTransaction = mySql.prepareStatement(getTransactionQuery);
            fetchTransaction.setInt(1, userId);
            fetchTransaction.setString(2, category);

            ResultSet res = fetchTransaction.executeQuery();

            populateList(transactions, res);

        } catch (SQLException | IOException e) {
            System.out.println("MySQL error: " + e.getMessage());
        }
        return transactions;
    }

    public static List<Transaction> getTransactionsByDate(int userId, Date start, Date end, boolean isTest) {
        //todo: implement these
        return null;
    }

    //helper method to add results from res into List
    private static void populateList(List<Transaction> list, ResultSet res) {
        try {
            while (res.next()) {
                int dbId = res.getInt(1);
                String dbDescription = res.getString(2);
                double dbAmount = res.getDouble(3);
                Date dbDate = res.getDate(4);
                int dbUserId = res.getInt(5);
                String dbCategory = res.getString(6);
                Transaction t = new Transaction(dbId, dbAmount, dbDescription, dbCategory, dbUserId, dbDate);
                list.add(t);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
