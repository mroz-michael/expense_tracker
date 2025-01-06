package com.expense_tracker.controllers;

import com.expense_tracker.Transaction;
import com.expense_tracker.services.TransactionService;
import com.expense_tracker.utils.InputValidator;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class TransactionInterface {

    private final static boolean NOT_TEST = false; //TransactionInterface not used for test methods
    private static final String[] EXPENSE_CATEGORIES = {"Restaurants", "Entertainment", "Shopping", "Rent", "Groceries",
            "Tuition", "Bills", "Transportation", "Investing", "Miscellaneous"};


    public static void createTransaction(int userId) {

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

                if (cancelRequest(inputAmount)) {
                    return;
                }

                amountValid = InputValidator.validDouble(inputAmount);
            }

            double amount = Double.parseDouble(inputAmount);

            System.out.print("Enter date (yyyy-mm-dd): ");
            String dateInput = scanner.nextLine().trim();
            boolean dateValid = InputValidator.validDate(dateInput);

            while (!dateValid) {
                System.out.print("Error: Transaction Date was not formatted correctly, please enter a date in the format:");
                System.out.println(" yyyy-mm-dd\nOr type 'cancel' to go back to the previous menu");
                dateInput = scanner.nextLine().trim();
                if (cancelRequest(dateInput)) {
                    return;
                }

                dateValid = InputValidator.validDate(dateInput);
            }

            Date date = Date.valueOf(dateInput);
            System.out.println("Please enter the number of the category that best matches the Transaction: ");
            printCategories();
            String catNumber = scanner.next();
            boolean numValid = InputValidator.validInt(catNumber, 1, EXPENSE_CATEGORIES.length);
            while (!numValid) {
                System.out.println("Invalid category number");
                System.out.println("Please enter the number of the category that best matches the Transaction: ");
                printCategories();
                System.out.println("Or type 'cancel' to go back to the previous menu");
                catNumber = scanner.nextLine().trim();

                if (cancelRequest(catNumber)) {
                    return;
                }

                numValid = InputValidator.validInt(catNumber, 1, EXPENSE_CATEGORIES.length);
            }
            int categoryIndex = Integer.valueOf(catNumber) - 1;
            if (categoryIndex < 0 || categoryIndex >= EXPENSE_CATEGORIES.length) {
                System.out.println("Input validation error, aborting.");
                return;
            }

            String category = EXPENSE_CATEGORIES[categoryIndex];

            TransactionService.createTransaction( amount, description, category, userId, date, NOT_TEST);

            System.out.println("Transaction added successfully!");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void printCategories() {

        for (int i = 0; i < EXPENSE_CATEGORIES.length; i++) {
            System.out.println( (i + 1) + ": " + EXPENSE_CATEGORIES[i]);
        }
    }

    public void displayAllTransactions(int userId) {
            List<Transaction> transactions = TransactionService.getAllTransactions(userId, NOT_TEST);

            if (transactions.isEmpty()) {
                System.out.println("No transactions found.");
                return;
            }

            System.out.println("Transactions:");

            for (Transaction transaction : transactions) {
                System.out.println(transaction);
            }
    }

    public void displayTransactionsByCategory(int userId) {
        Scanner inputScan = new Scanner(System.in);
        System.out.println("Please select the category number of the transactions you want to display:");
        printCategories();
        String inputInt = inputScan.next();
        boolean validInt = InputValidator.validInt(inputInt, 1, EXPENSE_CATEGORIES.length);
        while (!validInt) {
            System.out.println("Invalid category number");
            printCategories();
            System.out.println("Or type 'cancel' to go back to the previous menu");
            inputInt = inputScan.nextLine().trim();

            if (cancelRequest(inputInt)) {
                return;
            }
            validInt = InputValidator.validInt(inputInt, 1, EXPENSE_CATEGORIES.length);
        }
        String category = "";
        try {
            int categoryIndex = Integer.parseInt(inputInt) - 1;
            category = EXPENSE_CATEGORIES[categoryIndex];
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Input Validation error, aborting.");
            return;
        }

        List<Transaction> transactions = TransactionService.getTransactionsByCategory(userId, category, NOT_TEST);
        System.out.println("All " + category + " transactions:");
        for (Transaction t: transactions) {
            System.out.println(t);
        }
    }


    public static void displayTransactionsByAmount(int userId) {
        Scanner inputScan = new Scanner(System.in);
        System.out.println("Please enter the minimum amount of the transactions you want to display:");
        String inputMin = inputScan.next();
        boolean validDouble = InputValidator.validDouble(inputMin);
        double min = -1;
        double max = -1;

        while (!validDouble) {
            System.out.println("Invalid format for minimum transaction amount. Please enter a valid format");
            System.out.println("Or type 'cancel' to go back to the previous menu");
            inputMin = inputScan.nextLine().trim();
            if (cancelRequest(inputMin)) {
                return;
            }
            validDouble = InputValidator.validDouble(inputMin);
        }

        try {
            min = Double.parseDouble(inputMin);

        } catch (NumberFormatException e) {
            System.out.println("Input Validation error, aborting.");
            return;
        }

        System.out.println("Please enter the maximum amount of the transactions you want to display:");
        String inputMax = inputScan.next();
        validDouble = InputValidator.validDouble(inputMax);

        while (!validDouble) {
            System.out.println("Invalid format for maximum transaction amount. Please enter a valid format");
            System.out.println("Or type 'cancel' to go back to the previous menu");
            inputMax = inputScan.nextLine().trim();
            if (cancelRequest(inputMax)) {
                return;
            }
            validDouble = InputValidator.validDouble(inputMax);
        }

        try {
            max = Double.parseDouble(inputMax);
        } catch (NumberFormatException e) {
            System.out.println("Input Validation error, aborting.");
        }

        List<Transaction> transactions = TransactionService.getTransactionsByAmount(userId, min, max, NOT_TEST);
        System.out.println("All transactions between " + min + " and " + max +":");
        for (Transaction t: transactions) {
            System.out.println(t);
        }
    }

    public static void displayTransactionsByDate(int userId) {
        //todo: get input from user, call TransactionService
    }


    public void deleteTransactionFromInput() {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Enter transaction ID to delete: ");
            String inputInt = scanner.next();
            boolean validInt = InputValidator.validInt(inputInt, 1, Integer.MAX_VALUE);
            while (!validInt) {
                System.out.println("Malformatted transaction ID. please enter a positive integer");
                System.out.println("Or type 'cancel' to go back to the previous menu");
                inputInt = scanner.nextLine().trim();

                if (cancelRequest(inputInt)) {
                    return;
                }

                validInt = InputValidator.validInt(inputInt, 1, EXPENSE_CATEGORIES.length);
            }
            int tId = Integer.valueOf(inputInt);
            if (tId < 1) {
                System.out.println("Input validation error, aborting.");
                return;
            }

            boolean deleted = TransactionService.deleteTransaction(tId, NOT_TEST);
            String res = deleted ? "Transaction deleted successfully!" : "Transaction was not deleted properly";
            System.out.println(res);
        } catch (Exception e) {
            System.err.println("Error deleting transaction: " + e.getMessage());
        }
    }

    private static boolean cancelRequest(String input) {
        String formattedInput = input.toLowerCase().trim();
        return formattedInput.equals("cancel") || formattedInput.equals("'cancel'");
    }
}
