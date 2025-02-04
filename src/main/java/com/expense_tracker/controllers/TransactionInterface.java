package com.expense_tracker.controllers;

import com.expense_tracker.Transaction;
import com.expense_tracker.services.TransactionService;
import com.expense_tracker.utils.InputValidator;

import java.time.DateTimeException;
import java.util.List;
import java.time.LocalDate;
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

            String datePrompt = "Enter transaction date (yyyy mm dd): ";
            LocalDate date = getDateFromInput(datePrompt);

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
            if (categoryIndex < 0 || categoryIndex >= EXPENSE_CATEGORIES.length || date == null) {
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

    public static void displayAllTransactions(int userId) {
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

    public static void displayTransactionsByCategory(int userId) {
        Scanner inputScan = new Scanner(System.in);
        System.out.println("Please select the category number of the transactions you want to display:");
        printCategories();
        String inputInt = inputScan.nextLine();
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

        if (transactions.isEmpty()) {
            System.out.println("No transactions in the " + category + " category were found ");
            return;
        }

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
        String inputMax = inputScan.nextLine();
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

        if (transactions.isEmpty()) {
            System.out.println("No transactions were found within that amount range.");
            return;
        }

        System.out.println("All transactions between $" + min + " and $" + max +":");
        for (Transaction t: transactions) {
            System.out.println(t);
        }
    }

    public static void displayTransactionsByDate(int userId) {
        String startPrompt= "Enter the starting date for transactions you want to view: ";
        LocalDate startDate = getDateFromInput(startPrompt);
        String endPrompt = "Enter the ending date for transactions you want to view: ";
        LocalDate endDate = getDateFromInput(endPrompt);
        if (endDate == null || startDate == null) {
            System.out.println("Input validation error, aborting.");
            return;
        }

        List<Transaction> transactions = TransactionService.getTransactionsByDate(userId, startDate, endDate, NOT_TEST);

        if (transactions.isEmpty()) {
            System.out.println("No transactions were found between those dates.");
            return;
        }

        System.out.println("All transactions between " + startDate + " and " + endDate +":");
        for (Transaction t: transactions) {
            System.out.println(t);
        }
    }

    public static void updateTransactionFromInput() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter the ID of the transaction you wish to update.");
        String transactionIdString = scanner.nextLine().toLowerCase().trim();
        boolean isValidInt = InputValidator.validInt(transactionIdString, 1, Integer.MAX_VALUE);

        if (!isValidInt) {
            System.out.println("Invalid format for ID given");
            return;
        }

        int transactionId = Integer.parseInt(transactionIdString);

        Transaction transaction = TransactionService.getTransaction(transactionId, NOT_TEST);

        if (transaction == null) {
            System.out.println("Could not find a transaction with that ID");
            return;
        }

        System.out.println("Retrieved transaction\n"+ transaction);
        System.out.println("Please enter a number corresponding with the information you'd like to update");

        System.out.println("1: Edit Amount");
        System.out.println("2: Edit Description");
        System.out.println("3: Edit Category");
        System.out.println("4: Edit Date");
        String updateField = scanner.nextLine();
        isValidInt = InputValidator.validInt(updateField, 1, 4);
        if (!isValidInt) {
            System.out.println("Invalid number entered, cancelling update request.");
            return;
        }

        boolean wasUpdated = false;

        switch(updateField) {
            case "1":
                wasUpdated = updateAmount(transaction);
                break;
            case "2":
                wasUpdated = updateDescription(transaction);
                break;
            case "3":
                wasUpdated = updateCategory(transaction);
                break;
            case "4":
                wasUpdated = updateDate(transaction);
                break;
            default:
                break;
        }

        if (!wasUpdated) {
            //print relevant error message in updateX() methods
            return;
        }

        boolean updateSuccessful = TransactionService.updateTransaction(transaction, NOT_TEST);

        String result = updateSuccessful ? "Transaction updated successfully" : "Error updating transaction";
        System.out.println(result);
    }
    
    public static void deleteTransactionFromInput() {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Enter transaction ID to delete: ");
            String inputInt = scanner.next();
            boolean validInt = InputValidator.validInt(inputInt, 1, Integer.MAX_VALUE);
            while (!validInt) {
                System.out.println("Malformed transaction ID. please enter a positive integer");
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
            String res = deleted ? "Transaction deleted successfully" : "Transaction with given ID could not be deleted";
            System.out.println(res);
        } catch (Exception e) {
            System.err.println("Error deleting transaction: " + e.getMessage());
        }
    }

    /**
     * helper method to check if user wishes to cancel current event and return to previous menu
     * @param input the user's input to a command line prompt
     * @return true if and only if input is "cancel" or 'cancel'
     */
    private static boolean cancelRequest(String input) {
        String formattedInput = input.toLowerCase().trim();
        return formattedInput.equals("cancel") || formattedInput.equals("'cancel'");
    }

    private static LocalDate getDateFromInput(String promptMessage) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(promptMessage);
        System.out.println("\nPlease enter in format: YYYY MM DD");
        String inputDate = scanner.nextLine().trim();
        boolean validDate = InputValidator.validDate(inputDate);
        while (!validDate) {
            System.out.println("Malformed date. Please enter in format: YYYY MM DD");
            System.out.println("Or type 'cancel' to go back to the previous menu");
            inputDate = scanner.nextLine().trim();

            if (cancelRequest(inputDate)) {
                return null;
            }

            validDate = InputValidator.validDate(inputDate);
        }

        try {
            LocalDate date = LocalDate.parse(inputDate, InputValidator.DATE_FORMATTER);
            return date;
        } catch (DateTimeException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static boolean updateAmount(Transaction t) {
        System.out.println("Enter the updated amount for this transaction.");
        Scanner scanner = new Scanner(System.in);
        String amountInput = scanner.next();
        boolean isValid = InputValidator.validDouble(amountInput);
        if (!isValid) {
            System.out.println("Invalid input given for new amount, cancelling transaction update.");
        } else {
            double amount = Double.parseDouble(amountInput);
            t.setAmount(amount);
        }

        return isValid;
    }

    public static boolean updateDescription(Transaction t) {
        System.out.println("Enter the updated description for this transaction. Hit Enter when done.");
        Scanner scanner = new Scanner(System.in);
        String newDescription = scanner.nextLine();
        t.setDescription(newDescription);
        return true;
    }

    public static boolean updateCategory(Transaction t) {
        System.out.println("Select the number of the updated category for this transaction.");
        printCategories();
        Scanner scanner = new Scanner(System.in);
        String catNum = scanner.next();
        boolean isValid = InputValidator.validInt(catNum, 1, EXPENSE_CATEGORIES.length);
        if (!isValid) {
            System.out.println("Invalid category number, cancelling transaction update.");
        } else {
            int catIndex = Integer.valueOf(catNum) - 1;
            t.setCategory(EXPENSE_CATEGORIES[catIndex]);
        }

        return isValid;
    }

    public static boolean updateDate(Transaction t) {
        LocalDate date = getDateFromInput("Please enter the updated date for this transaction");
        if (date != null) {
            t.setDate(date);
        }

        return date != null;
    }
}