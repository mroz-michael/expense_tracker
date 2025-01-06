package com.expense_tracker.services;

import com.expense_tracker.Transaction;
import com.expense_tracker.db.TransactionQueryExecutor;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

//perform CRUD operations on transactions by calling Query Executor
public class TransactionService {

    public static Transaction createTransaction(double amount, String description, String category, int userId, LocalDate date, boolean isTest) {
        return TransactionQueryExecutor.createTransaction(amount, description, category, userId, date, isTest);
    }

    public static Transaction createTransaction(double amount, String description, String category, int userId, boolean isTest) {
        LocalDate date = LocalDate.now();
        return createTransaction(amount, description, category, userId, date, isTest);
    }

    public static Transaction getTransaction(int id, boolean isTest) {
        return TransactionQueryExecutor.getTransaction(id, isTest);
    }

    public static List<Transaction> getAllTransactions(int userId, boolean isTest) {
        return TransactionQueryExecutor.getAllTransactions(userId, isTest);
    }
    
    public static List<Transaction> getTransactionsByAmount(int userId, double min, double max, boolean isTest) {
        return TransactionQueryExecutor.getTransactionsByAmount(userId, min, max, isTest);
    }

    public static List<Transaction> getTransactionsByCategory(int userId, String category, boolean isTest) {
        return TransactionQueryExecutor.getTransactionsByCategory(userId, category, isTest);
    }


    public static List<Transaction> getTransactionsByDate(int userId, LocalDate start, LocalDate end, boolean isTest) {
        return TransactionQueryExecutor.getTransactionsByDate(userId, start, end, isTest);
    }

    public static boolean updateTransaction(Transaction updatedTransaction, boolean isTest) {
        return TransactionQueryExecutor.updateTransaction(updatedTransaction, isTest);
    }

    public static boolean deleteTransaction(int transactionId, boolean isTest) {
        return TransactionQueryExecutor.deleteTransaction(transactionId, isTest);
    }
}
