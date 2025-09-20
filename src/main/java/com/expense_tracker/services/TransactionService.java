package com.expense_tracker.services;

import com.expense_tracker.Transaction;
import com.expense_tracker.db.TransactionQueryExecutor;

import java.time.LocalDate;
import java.util.List;

//perform CRUD operations on transactions by calling Query Executor
public class TransactionService {

    private TransactionQueryExecutor queryExecutor;

    public TransactionService(TransactionQueryExecutor executor) {
        this.queryExecutor = executor;
    }

    public Transaction createTransaction(double amount, String description, String category, int userId, LocalDate date) {
        return queryExecutor.createTransaction(amount, description, category, userId, date );
    }

    public Transaction createTransaction(double amount, String description, String category, int userId) {
        LocalDate date = LocalDate.now();
        return createTransaction(amount, description, category, userId, date);
    }

    public Transaction getTransaction(int id) {
        return queryExecutor.getTransaction(id);
    }

    public List<Transaction> getAllTransactions(int userId) {
        return queryExecutor.getAllTransactions(userId);
    }
    
    public  List<Transaction> getTransactionsByAmount(int userId, double min, double max) {
        return queryExecutor.getTransactionsByAmount(userId, min, max);
    }

    public List<Transaction> getTransactionsByCategory(int userId, String category) {
        return queryExecutor.getTransactionsByCategory(userId, category);
    }


    public List<Transaction> getTransactionsByDate(int userId, LocalDate start, LocalDate end) {
        return queryExecutor.getTransactionsByDate(userId, start, end);
    }

    public boolean updateTransaction(Transaction updatedTransaction) {
        return queryExecutor.updateTransaction(updatedTransaction);
    }

    public boolean deleteTransaction(int transactionId) {
        return queryExecutor.deleteTransaction(transactionId);
    }
}
