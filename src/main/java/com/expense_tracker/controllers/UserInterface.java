package com.expense_tracker.controllers;

import com.expense_tracker.User;
import com.expense_tracker.db.UserQueryExecutor;
import com.expense_tracker.services.AuthenticationService;
import com.expense_tracker.utils.InputValidator;


import java.util.Map;
import java.util.Scanner;

public class UserInterface {

    //map input commands to their menu-description
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

    private TransactionInterface transactionInterface;
    private AuthenticationService authService;
    private UserQueryExecutor userExecutor;
    private final Scanner scanner;

    public UserInterface(TransactionInterface transactionInterface, AuthenticationService authService, UserQueryExecutor userExecutor) {
        this.transactionInterface = transactionInterface;
        this.authService = authService;
        this.userExecutor = userExecutor;
        this.scanner = new Scanner(System.in);
    }

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
    public String getLoginPrompt() {
        
        String cmd = scanner.nextLine().toLowerCase().trim();
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
            cmd = scanner.nextLine().toLowerCase().trim();
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
    public User login() {
        System.out.println("Logging in: ");
        System.out.print("Please enter your username: ");
        String username = scanner.next().trim();
        System.out.print("\nPlease enter your password: ");
        String plainTxtPw = scanner.next().trim();
        System.out.println();
        User loggedInUser = authService.login(username, plainTxtPw);

        return loggedInUser;
    }
    public User register() {
        System.out.print("Enter the username you'd like: ");
        String username = scanner.nextLine().trim();
        System.out.println("\nPlease create a password for this account (4 character minimum)");
        String unHashedPw = scanner.nextLine().trim();
        while  (unHashedPw.length() < 4) {
            System.out.println("Password must be at least 4 characters");
            System.out.println("Please create a password for this account (4 character minimum)");
            unHashedPw = scanner.nextLine().trim();
        }
        System.out.println();
        return authService.createUser(username, unHashedPw);
    }

    public void promptUsernameChange(User user) {
        System.out.print("Are you sure you want to change your username? This is a non-reversible action");
        System.out.println("Type Y or YES to confirm");
        
        String userAgreement = scanner.next();
        if (! (userAgreement.toUpperCase().equals("Y") || userAgreement.toUpperCase().equals("YES") )) {
            System.out.println("Username change request cancelled by user.");
            return;
        }
        //send existing and new username to db to confirm uniqueness and update
        System.out.print("Please enter your new username: ");
        String newUsername = scanner.next().trim();
        System.out.println("\nProcessing request...");
        user.setUsername(newUsername);
        boolean userUpdated = userExecutor.updateUser(user);
        String res = userUpdated ? "Username changed successfully" : "Error changing username, request cancelled";
        System.out.println(res);
    }

    public void promptPasswordChange(User user) {
        System.out.println("To change your password, first enter your current password.");
        String oldPassword = scanner.next().trim();
        String username = user.getUsername();
        boolean passwordIsValid = authService.validatePassword(username, oldPassword);

        int attemptsRemaining = 5;
        while (!passwordIsValid && attemptsRemaining > 0) {
            System.out.println("Incorrect password, please try again: ");
            oldPassword = scanner.next().trim();
            passwordIsValid = authService.validatePassword(username, oldPassword);
            attemptsRemaining--;
        }

        if (!passwordIsValid) {
            System.out.println("Too many incorrect attempts. Please confirm your password before trying again.");
            //todo: add some sort of account recovery mechanism (email or security question)
            return;
        }

        System.out.print("Please enter your new password: ");
        String newPw = scanner.next().trim();
        while (newPw.length() < 4) {
            System.out.print("\nPlease enter a longer password (4 characters minimum)");
            newPw = scanner.next().trim();
        }

        String newPwHash = AuthenticationService.generateHash(newPw);
        user.setPwHash(newPwHash);
        boolean userUpdated = userExecutor.updateUser(user);
        String response = userUpdated ? "\nPassword updated successfully" : "\nError: Database failed to save new password";
        System.out.println(response);
    }

    public  boolean promptDeleteUser(User user) {
        System.out.println("Are you sure you want to delete this account? This is a non-recoverable action");
        System.out.println("Please confirm by entering YES or Y");
        String confirmation = scanner.nextLine().trim();
        boolean wasDeleted = false;
        if (confirmation.toUpperCase().trim().equals("YES") || confirmation.toUpperCase().trim().equals("Y")) {
            System.out.println("Please enter your password to finalize account deletion");
            String plainPw = scanner.nextLine().trim();
            boolean pwValid = authService.validatePassword(user.getUsername(), plainPw);

            if (pwValid) {
                wasDeleted = userExecutor.deleteUser(user);
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
    public void promptTransactionQuery(int userId) {
        
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
            transactionInterface.displayAllTransactions(userId);
            return;
        }

        if (query.equals("all")) {
            System.out.println("Displaying All Transactions");
            transactionInterface.displayAllTransactions(userId);
            return;
        }

        if (query.equals("date")) {
            transactionInterface.displayTransactionsByDate(userId);
            return;
        }

        if (query.equals("amount")) {
            transactionInterface.displayTransactionsByAmount(userId);
            return;
        }

        if (query.equals("category")) {
            transactionInterface.displayTransactionsByCategory(userId);
            return;
        }

        System.out.println("An unexpected error occurred, cancelling request to view transactions.");
    }

    private static void printInvalidInputMessage() {
        System.out.println("Error: Invalid input");
    }
}
