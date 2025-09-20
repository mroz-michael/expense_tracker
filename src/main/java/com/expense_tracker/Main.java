package com.expense_tracker;

import com.expense_tracker.controllers.TransactionInterface;
import com.expense_tracker.controllers.UserInterface;
import com.expense_tracker.db.DBConnector;
import com.expense_tracker.db.TransactionQueryExecutor;
import com.expense_tracker.db.UserQueryExecutor;
import com.expense_tracker.utils.InputValidator;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main( String[] args ) {

        try {
            System.out.println("\nWelcome to the Expense Tracker Application\n");
            User currUser = null;
            while (currUser == null) {
                UserInterface.displayLoginScreen();
                String cmd = UserInterface.getLoginPrompt();
                if (cmd.equals("exit")) {
                    return;
                }

                currUser = cmd.equals("register") ? UserInterface.register() : UserInterface.login();
                if (currUser == null) {
                    System.out.println("Invalid username or password, please try again.");
                }
            }
            executeMainLoop(currUser);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void executeMainLoop(User user) {
            int userId = user.getId();
            Scanner input = new Scanner(System.in);

            //build each layer bottom up
            Connection connection = DBConnector.connect();

            TransactionQueryExecutor transactionExecutor = new TransactionQueryExecutor(connection);

            //todo: brainstorm a less-daunting name
            UserQueryExecutor userExecutor = new UserQueryExecutor(connection);

            

            UserInterface.displayMainMenu();
        while (true) {

            System.out.println("\nPlease hit enter to view the available command numbers, or type a command number if known");
            System.out.println("Or type 'exit' to end the program.");
            String cmd = input.nextLine().trim();
            boolean validInput = InputValidator.validCommand(cmd);
            while (!validInput) {
                UserInterface.displayMainMenu();
                cmd = input.nextLine().trim();
                validInput = InputValidator.validCommand(cmd);
            }

            //UserInterface.COMMAND_LIST is mapping of numbers to commands
            switch (cmd) {
                case "1": //create transaction
                    transactionInterface.createTransaction(userId);
                    break;
                case "2": //get transactions based on query
                    UserInterface.promptTransactionQuery(userId);
                    break;
                case "3": //update transaction
                    TransactionInterface.updateTransactionFromInput();
                    break;
                case "4": //delete transaction
                    TransactionInterface.deleteTransactionFromInput();
                    break;
                case "5": //view account details
                    System.out.println("*** Account Details ***");
                    System.out.println(user);
                    System.out.println("***********************");
                    break;
                case "6", "exit", "'exit'": //logout
                    System.out.println("Exiting the program, goodbye.");
                    return;
                case "7": //change username
                    UserInterface.promptUsernameChange(user);
                    break;
                case "8": //change pw
                    UserInterface.promptPasswordChange(user);
                    break;
                case "9": //delete account :(
                    boolean userDeleted = UserInterface.promptDeleteUser(user);
                    if (userDeleted) {
                        return;
                    }
                    System.out.println("Could not validate user deletion, returning to menu.");
                    break;
                default: break;
            }
        }
    }
}