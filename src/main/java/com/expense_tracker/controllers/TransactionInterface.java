package com.expense_tracker.controllers;

import com.expense_tracker.Transaction;
import com.expense_tracker.services.TransactionService;
import com.expense_tracker.utils.InputValidator;

import java.util.List;
import java.util.Scanner;

public class TransactionInterface {

    public static void createTransaction() {

        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Enter description: ");
            String description = scanner.nextLine().trim();

            System.out.print("Enter amount: ");
            String inputAmount = scanner.nextLine().trim();
            boolean amountValid = InputValidator.validDouble(inputAmount);

            while (!amountValid) {
                System.out.println("Error, Transaction amount was not formatted correctly, please enter a proper numeric amount");
                System.out.println("Or type 'cancel' to go back to the previous menu");
                inputAmount = scanner.nextLine().trim();
                if (cancelRequest(inputAmount)) {return;}
                amountValid = InputValidator.validDouble(inputAmount);
            }

            double amount = Double.parseDouble(inputAmount);

            System.out.print("Enter date (yyyy-mm-dd): ");
            String dateInput = scanner.nextLine().trim();
            boolean dateValid = InputValidator.validDate(dateInput);

            while (!dateValid) {
                System.out.print("Error, Transaction Date was not formatted correctly, please enter a date in the format:");
                System.out.println(" yyyy-mm-dd\nOr type 'cancel' to go back to the previous menu");
                dateInput = scanner.nextLine().trim();
                if (cancelRequest(dateInput)) {return;}
                dateValid = InputValidator.validDate(dateInput);
            }

            System.out.print("Enter category: ");
            String category = scanner.next();

            //Transaction transaction = TransactionService.createTransaction();

            System.out.println("Transaction added successfully!");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // Display all transactions
    public void displayAllTransactions() {
        try {
            List<Transaction> transactions = TransactionService.getTransactions();

            if (transactions.isEmpty()) {
                System.out.println("No transactions found.");
                return;
            }

            System.out.println("Transactions:");
            for (Transaction transaction : transactions) {
                System.out.println(transaction);
            }
        } catch (Exception e) {
            System.err.println("Error fetching transactions: " + e.getMessage());
        }
    }

    public void deleteTransactionFromInput() {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Enter transaction ID to delete: ");
            int id = scanner.nextInt();

            //todo add this method => TransactionService.deleteTransaction();
            System.out.println("Transaction deleted successfully!");
        } catch (Exception e) {
            System.err.println("Error deleting transaction: " + e.getMessage());
        }
    }

    private static boolean cancelRequest(String input) {
        String formattedInput = input.toLowerCase().trim();
        return formattedInput.equals("cancel") || formattedInput.equals("'cancel'");
    }



}
