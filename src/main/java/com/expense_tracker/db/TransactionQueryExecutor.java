package com.expense_tracker.db;

import com.expense_tracker.Transaction;
import com.expense_tracker.User; //pass user to check user.id || user.type == owner/admin
import java.util.ArrayList;
import java.util.List;

public class TransactionQueryExecutor {

    public static Transaction createTransaction(double amount, User user,  String category, String description, boolean isTest)
    { //todo: add other args
        return null;
    }

    public static Transaction getTransaction(int id, boolean isTest) {
        return null;
    }

    public static Transaction getTransaction(int id, User user, boolean isTest) {
        return null;
    }

    public static List<Transaction> getAllTransactions(int id, User user, boolean isTest) {
        List<Transaction> transactions = new ArrayList<>();
        return transactions;
    }

    public static Transaction updateTransaction(int id, Transaction updatedTransaction, boolean isTest) {
        return null;
    }

    public static boolean deleteTransaction(int id, User user, boolean isTest) {
        return false;
    }

}
