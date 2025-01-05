package com.expense_tracker.services;

import com.expense_tracker.Transaction;
import com.expense_tracker.db.TransactionQueryExecutor;

import java.sql.Date;
import java.util.List;

//perform CRUD operations on transactions by calling Query Executor
public class TransactionService {

    public static Transaction createTransaction(double amount, String description, String category, int userId, Date date, boolean isTest) {
        return TransactionQueryExecutor.createTransaction(amount, description, category, userId, date, isTest);
    }

    public static Transaction createTransaction(double amount, String description, String category, int userId, boolean isTest) {
        Date date = new Date(System.currentTimeMillis());
        return createTransaction(amount, description, category, userId, date, isTest);
    }

    public static Transaction getTransaction(int id, boolean isTest) {
        return TransactionQueryExecutor.getTransaction(id, isTest);
    }

    public static List<Transaction> getAllTransactions(int userId, boolean isTest) {
        return TransactionQueryExecutor.getAllTransactions(userId, isTest);
    }

    public static boolean updateTransaction(Transaction updatedTransaction, boolean isTest) {
        return TransactionQueryExecutor.updateTransaction(updatedTransaction, isTest);
    }

    public static boolean deleteTransaction() {
        return false;
    }
}
