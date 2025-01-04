package com.expense_tracker.services;

import com.expense_tracker.Transaction;
import com.expense_tracker.db.TransactionQueryExecutor;

import java.util.Date;
import java.util.List;

//perform CRUD operations on transactions by calling Query Executor
public class TransactionService {

    public static Transaction createTransaction(double amount, String description, String category, Date date) {
       return null;
    }

    public static Transaction getTransaction() {
        return null;
    }

    public static List<Transaction> getTransactions() {
        return null;
    }

    public static boolean updateTransaction() {
        return false;
    }

    public static boolean deleteTransaction() {
        return false;
    }
}
