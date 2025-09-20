package com.expense_tracker.db;

import com.expense_tracker.Transaction;
import com.expense_tracker.utils.ConfigLoader;

import java.sql.*;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;

public class TransactionQueryExecutor {

    private final Connection mySql;

    public TransactionQueryExecutor(Connection mysql) {
        this.mySql = mysql;
    }


    public Transaction createTransaction(double amount, String description, String category, int userId) {
        LocalDate date = LocalDate.now();
        return createTransaction(amount, description, category, userId, date);
    }
    public Transaction createTransaction(
            double amount,
            String description,
            String category,
            int userId,
            LocalDate date
        ) {
            try {
                
                String tableName = getTableName();
                String createTransactionQuery = TransactionQueries.create(tableName);
                PreparedStatement newTransaction = mySql.prepareStatement(createTransactionQuery, PreparedStatement.RETURN_GENERATED_KEYS);

                newTransaction.setDouble(1, amount);
                newTransaction.setString(2, description);
                newTransaction.setString(3, category);
                newTransaction.setInt(4, userId);
                newTransaction.setDate(5, Date.valueOf(date));
                newTransaction.executeUpdate();

                ResultSet res = newTransaction.getGeneratedKeys();

                int id = -1;
                while (res.next()) {
                    id = res.getInt(1);
                }

                Transaction createdTransaction = getTransaction(id);

                return createdTransaction;
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
                return null;
            }
    }

    public Transaction getTransaction(int id) {

        try {
            String tableName = getTableName();
            String getTransactionQuery = TransactionQueries.getOne(tableName);
            PreparedStatement fetchTransaction = mySql.prepareStatement(getTransactionQuery);
            fetchTransaction.setInt(1, id);
            ResultSet res = fetchTransaction.executeQuery();

            int dbId = -1;
            String dbDescription = "";
            double dbAmount = 0.00;
            LocalDate dbDate = null;
            int dbUserId = -1;
            String dbCategory = "";

            while (res.next()) {
                dbId = res.getInt(1);
                dbDescription = res.getString(2);
                dbAmount = res.getDouble(3);
                dbDate = res.getDate(4).toLocalDate();
                dbUserId = res.getInt(5);
                dbCategory = res.getString(6);
            }

            if (dbId == -1) {
                return null;
            }

            return new Transaction(dbId, dbAmount, dbDescription, dbCategory, dbUserId, dbDate);

        } catch (SQLException e) {
            System.out.println("MySQL error: " + e.getMessage());
            return null;
        }
    }

    public  List<Transaction> getAllTransactions(int userId) {
        List<Transaction> transactions = new ArrayList<>();

        try {
            String tableName = getTableName();
            String getTransactionQuery = TransactionQueries.getAll(tableName);
            PreparedStatement fetchTransaction = mySql.prepareStatement(getTransactionQuery);
            fetchTransaction.setInt(1, userId);

            ResultSet res = fetchTransaction.executeQuery();
            populateList(transactions, res);

        } catch (SQLException e) {
            System.out.println("MySQL error: " + e.getMessage());
        }
        return transactions;
    }

    public  boolean updateTransaction(Transaction updatedTransaction) {
        try {
            String tableName = getTableName();
            String updateTransactionQuery = TransactionQueries.update(tableName);

            PreparedStatement updateTransaction = mySql.prepareStatement(updateTransactionQuery);
            updateTransaction.setDouble(1, updatedTransaction.getAmount());
            updateTransaction.setString(2, updatedTransaction.getDescription());
            updateTransaction.setString(3, updatedTransaction.getCategory());
            updateTransaction.setInt(4, updatedTransaction.getId());

            int rowsEffected = updateTransaction.executeUpdate();
            return rowsEffected > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    public  boolean deleteTransaction(int transactionId) {

        try {
            
            String tableName = getTableName();
            String deleteUserQuery = TransactionQueries.delete(tableName);
            PreparedStatement removeUser = mySql.prepareStatement(deleteUserQuery);
            removeUser.setInt(1, transactionId);
            int rowsEffected = removeUser.executeUpdate();

            return rowsEffected == 1;

        } catch (SQLException e) {
            System.out.println("Error attempting to delete transaction: " + e.getMessage());
        }

        return false;
    }

    public List<Transaction> getTransactionsByAmount(int userId, double min, double max) {
        List<Transaction> transactions = new ArrayList<>();

        try {

            
            String tableName = getTableName();
            String getTransactionQuery = TransactionQueries.getByAmount(tableName);
            PreparedStatement fetchTransaction = mySql.prepareStatement(getTransactionQuery);
            fetchTransaction.setInt(1, userId);
            fetchTransaction.setDouble(2, min);
            fetchTransaction.setDouble(3, max);

            ResultSet res = fetchTransaction.executeQuery();
            populateList(transactions, res);

        } catch (SQLException e) {
            System.out.println("MySQL error: " + e.getMessage());
        }

        return transactions;
    }

    public List<Transaction> getTransactionsByCategory(int userId, String category) {
        List<Transaction> transactions = new ArrayList<>();

        try {

            String tableName = getTableName();
            String getTransactionQuery = TransactionQueries.getByCategory(tableName);
            PreparedStatement fetchTransaction = mySql.prepareStatement(getTransactionQuery);
            fetchTransaction.setInt(1, userId);
            fetchTransaction.setString(2, category);

            ResultSet res = fetchTransaction.executeQuery();

            populateList(transactions, res);

        } catch (SQLException e) {
            System.out.println("MySQL error: " + e.getMessage());
        }

        return transactions;
    }

    public List<Transaction> getTransactionsByDate(int userId, LocalDate start, LocalDate end) {
        List<Transaction> transactions = new ArrayList<>();

        try {

            String tableName = getTableName();
            String getTransactionQuery = TransactionQueries.getByDate(tableName);
            PreparedStatement fetchTransaction = mySql.prepareStatement(getTransactionQuery);
            fetchTransaction.setInt(1, userId);
            fetchTransaction.setDate(2, Date.valueOf(start));
            fetchTransaction.setDate(3, Date.valueOf(end));

            ResultSet res = fetchTransaction.executeQuery();

            populateList(transactions, res);

        } catch (SQLException e) {
            System.out.println("MySQL error: " + e.getMessage());
        }

        return transactions;
    }

    private static String getTableName() {
        ConfigLoader config = ConfigLoader.getInstance();
        String tableSuffix = config.getTableSuffix();
        TableNameProvider provider = new TableNameProvider(tableSuffix);
        return provider.provideTransactionsTable();
    }

    //helper method to add results from res into List
    private static void populateList(List<Transaction> list, ResultSet res) {
        try {
            while (res.next()) {
                int dbId = res.getInt(1);
                String dbDescription = res.getString(2);
                double dbAmount = res.getDouble(3);
                LocalDate dbDate = res.getDate(4).toLocalDate();
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
