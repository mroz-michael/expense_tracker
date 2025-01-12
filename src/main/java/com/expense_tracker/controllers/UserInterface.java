package com.expense_tracker.controllers;

import com.expense_tracker.User;
import com.expense_tracker.db.UserQueryExecutor;
import com.expense_tracker.services.AuthenticationService;
import com.expense_tracker.utils.InputValidator;


import java.util.Map;
import java.util.Scanner;

public class UserInterface {

    private final static boolean NOT_TEST = false; //UserInterface not used for test methods

    private final static Map<String, String> COMMAND_LIST = Map.of(
            "1", "Enter a Transaction",
            "2", "View Existing Transactions",
            "3", "Update an Existing Transaction",
            "4", "Delete an Existing Transaction",
            "5", "View Account Details",
            "6", "Log out",
            "7", "Change Username",
            "8", "Change Password",
            "9", "Delete Account"
            //todo: "10", "Generate an Expense Report"
    );

    private static final String[] VALID_QUERIES = {"all", "date", "amount", "category"};

    public static int numCommands() {
        return COMMAND_LIST.size();
    }

    public static String[] getValidQueries() {
        return VALID_QUERIES;
    }

    public static void displayLoginScreen() {
        System.out.println("Please Enter One of the Following Commands to Login, Register or Exit the program: ");
        System.out.println("1: Login");
        System.out.println("2: Register");
        System.out.println("3: Exit");
    }

    /**
     * called on program start or after user logs out, allows for user to enter either cmd number or cmd itself
     * @return user's command, either "login", "register", or "exit"
     */
    public static String getLoginPrompt() {
        Scanner input = new Scanner(System.in);
        String cmd = input.nextLine().toLowerCase().trim();
        while (true) {
            if (cmd.equals("1") || cmd.equals("login")) {
                return "login";
            }
            if (cmd.equals("2") || cmd.equals("register")) {
                return "register";
            }
            if (cmd.equals("3") || cmd.equals("exit")) {
                return "exit";
            }
            printInvalidInputMessage();
            System.out.println();
            displayLoginScreen();
            cmd = input.nextLine().toLowerCase().trim();
        }
    }
    public static void displayMainMenu() {
        System.out.println("Please Enter One of the Following Command Numbers: ");
        for (int i = 1; i <= COMMAND_LIST.size(); i++) {
            String cmdNum = String.valueOf(i);
            System.out.println(i + ": " + COMMAND_LIST.get(cmdNum));
        }
    }

    /**
     * find a user based on inputted username and password
     * @return the User found in the database, or null if not found
     */
    public static User login() {
        System.out.println("Logging in: ");
        System.out.print("Please enter your username: ");
        Scanner inputScan = new Scanner(System.in);
        String username = inputScan.next().trim();
        System.out.print("\nPlease enter your password: ");
        String plainTxtPw = inputScan.next().trim();
        System.out.println();
        User loggedInUser = AuthenticationService.login(username, plainTxtPw, NOT_TEST);
        return loggedInUser;
    }
    public static User register() {
        Scanner inputScan = new Scanner(System.in);
        System.out.print("Enter the username you'd like: ");
        String username = inputScan.nextLine().trim();
        System.out.println("\nPlease create a password for this account (4 character minimum)");
        String unHashedPw = inputScan.nextLine().trim();
        while  (unHashedPw.length() < 4) {
            System.out.println("Password must be at least 4 characters");
            System.out.println("Please create a password for this account (4 character minimum)");
            unHashedPw = inputScan.nextLine().trim();
        }
        System.out.println();
        return AuthenticationService.createUser(username, unHashedPw, NOT_TEST);
    }

    public static void promptUsernameChange(User user) {
        System.out.print("Are you sure you want to change your username? This is a non-reversible action");
        System.out.println("Type Y or YES to confirm");
        Scanner inputScan = new Scanner(System.in);
        String userAgreement = inputScan.next();
        if (! (userAgreement.toUpperCase().equals("Y") || userAgreement.toUpperCase().equals("YES") )) {
            System.out.println("Username change request cancelled by user.");
            return;
        }
        //send existing and new username to db to confirm uniqueness and update
        System.out.print("Please enter your new username: ");
        String newUsername = inputScan.next().trim();
        System.out.println("\nProcessing request...");
        user.setUsername(newUsername);
        boolean userUpdated = UserQueryExecutor.updateUser(user, NOT_TEST);
        String res = userUpdated ? "Username changed successfully" : "Error changing username, request cancelled";
        System.out.println(res);
    }

    public static void promptPasswordChange(User user) {
        System.out.println("To change your password, first enter your current password.");
        Scanner inputScan = new Scanner(System.in);
        String oldPassword = inputScan.next().trim();
        String username = user.getUsername();
        boolean passwordIsValid = AuthenticationService.validatePassword(username, oldPassword, NOT_TEST);

        int attemptsRemaining = 5;
        while (!passwordIsValid && attemptsRemaining > 0) {
            System.out.println("Incorrect password, please try again: ");
            oldPassword = inputScan.next().trim();
            passwordIsValid = AuthenticationService.validatePassword(username, oldPassword, NOT_TEST);
            attemptsRemaining--;
        }

        if (!passwordIsValid) {
            System.out.println("Too many incorrect attempts. Please confirm your password before trying again.");
            //todo: add some sort of account recovery mechanism (email or security question)
            return;
        }

        System.out.print("Please enter your new password: ");
        String newPw = inputScan.next().trim();
        while (newPw.length() < 4) {
            System.out.print("\nPlease enter a longer password (4 characters minimum)");
            newPw = inputScan.next().trim();
        }

        String newPwHash = AuthenticationService.generateHash(newPw);
        user.setPwHash(newPwHash);
        boolean userUpdated = UserQueryExecutor.updateUser(user, NOT_TEST);
        String response = userUpdated ? "\nPassword updated successfully" : "\nError: Database failed to save new password";
        System.out.println(response);
    }

    public static boolean promptDeleteUser(User user) {
        System.out.println("Are you sure you want to delete this account? This is a non-recoverable action");
        System.out.println("Please confirm by entering YES or Y");
        Scanner scanner = new Scanner(System.in);
        String confirmation = scanner.nextLine().trim();
        boolean wasDeleted = false;
        if (confirmation.toUpperCase().trim().equals("YES") || confirmation.toUpperCase().trim().equals("Y")) {
            System.out.println("Please enter your password to finalize account deletion");
            String plainPw = scanner.nextLine().trim();
            boolean pwValid = AuthenticationService.validatePassword(user.getUsername(), plainPw, NOT_TEST);

            if (pwValid) {
                wasDeleted = UserQueryExecutor.deleteUser(user, NOT_TEST);
            }

            else {
                System.out.println("Confirmation not received, cancelling request to delete account.");
            }

        }

        return wasDeleted;
    }

    /**
     * prompt the user to either: view all transactions or filter by date|category|amount
     * @param userId
     */
    public static void promptTransactionQuery(int userId) {
        Scanner scanner = new Scanner(System.in);
        String query = "";
        boolean validQuery = InputValidator.validateQuery(query);

        System.out.println("Which transactions would you like to view?");
        System.out.println("Please enter one of the following command numbers:");
        System.out.println("1: View All Transactions (default)");
        System.out.println("2: View Transactions by Date");
        System.out.println("3: View Transactions by Amount");
        System.out.println("4: View Transactions by Category");
        String[] VALID_QUERIES = getValidQueries();
        query = scanner.nextLine();
        switch(query) {
            case "1", "2", "3", "4":
                query = VALID_QUERIES[Integer.valueOf(query) -1];
                //fall through after reassigning query
            default:
                validQuery = InputValidator.validateQuery(query);
        }

        if (!validQuery) {
            System.out.println("Invalid input, listing all transactions by default");
            TransactionInterface.displayAllTransactions(userId);
            return;
        }

        if (query.equals("all")) {
            System.out.println("Displaying All Transactions");
            TransactionInterface.displayAllTransactions(userId);
            return;
        }

        if (query.equals("date")) {
            TransactionInterface.displayTransactionsByDate(userId);
            return;
        }

        if (query.equals("amount")) {
            TransactionInterface.displayTransactionsByAmount(userId);
            return;
        }

        if (query.equals("category")) {
            TransactionInterface.displayTransactionsByCategory(userId);
            return;
        }

        System.out.println("An unexpected error occurred, cancelling request to view transactions.");
    }
    private static void printInvalidInputMessage() {
        System.out.println("Error: Invalid input");
    }
}
