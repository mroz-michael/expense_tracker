package com.expense_tracker;

import com.expense_tracker.controllers.TransactionInterface;
import com.expense_tracker.controllers.UserInterface;
import com.expense_tracker.db.DBConnector;
import com.expense_tracker.db.TableNameProvider;
import com.expense_tracker.db.TransactionQueryExecutor;
import com.expense_tracker.db.UserQueryExecutor;
import com.expense_tracker.services.AuthenticationService;
import com.expense_tracker.services.TransactionService;
import com.expense_tracker.utils.ConfigLoader;
import com.expense_tracker.utils.InputValidator;

import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main( String[] args ) {

        try {
            System.out.println("\nWelcome to the Expense Tracker Application\n");
            
            //build each layer bottom up
            Connection connection = DBConnector.connect();
            ConfigLoader configLoader = ConfigLoader.getInstance();

            //config.properties defines tableSuffix as "" for real data or "_test" for using dummy data in the main DB
            String tableSuffix = configLoader.getTableSuffix();

            TableNameProvider provider = new TableNameProvider(tableSuffix);

            TransactionQueryExecutor transactionExecutor = new TransactionQueryExecutor(connection, provider);
            UserQueryExecutor userExecutor = new UserQueryExecutor(connection, provider);

            TransactionService transactionService = new TransactionService(transactionExecutor);
            AuthenticationService authService = new AuthenticationService(userExecutor);

            TransactionInterface transactionInterface = new TransactionInterface(transactionService);
            UserInterface ui = new UserInterface(transactionInterface, authService, userExecutor);

            User currUser = null;
            while (currUser == null) {
                UserInterface.displayLoginScreen();
                String cmd = UserInterface.getLoginPrompt();
                if (cmd.equals("exit")) {
                    return;
                }

                currUser = cmd.equals("register") ? ui.register() : ui.login();
                if (currUser == null) {
                    System.out.println("Invalid username or password, please try again.");
                }
            }

            executeMainLoop(currUser, transactionInterface, ui);

        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void executeMainLoop(User user, TransactionInterface transactionInterface, UserInterface ui) {
            int userId = user.getId();
            Scanner input = new Scanner(System.in);

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
                    ui.promptTransactionQuery(userId);
                    break;
                case "3": //update transaction
                    transactionInterface.updateTransactionFromInput();
                    break;
                case "4": //delete transaction
                    transactionInterface.deleteTransactionFromInput();
                    break;
                case "5": //view account details
                    System.out.println("*** Account Details ***");
                    System.out.println(user);
                    System.out.println("***********************");
                    break;
                case "6", "exit", "'exit'": //logout
                    System.out.println("Exiting the program, goodbye.");
                    input.close();
                    return;
                case "7": //change username
                    ui.promptUsernameChange(user);
                    break;
                case "8": //change pw
                    ui.promptPasswordChange(user);
                    break;
                case "9": //delete account :(
                    boolean userDeleted = ui.promptDeleteUser(user);
                    if (userDeleted) {
                        input.close();
                        return;
                    }
                    System.out.println("Could not validate user deletion, returning to menu.");
                    break;
                default: break;
            }
        }
        
    }
}