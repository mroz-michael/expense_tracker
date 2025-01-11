package com.expense_tracker;

import com.expense_tracker.controllers.UserInterface;
import com.expense_tracker.db.DBConnector;
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

        while (true) {
            Scanner input = new Scanner(System.in);
            UserInterface.displayMainMenu();
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
                    //..todo
                    break;
                case "2": //get transaction
                    //..todo
                    break;
                case "3": //update transaction
                    //..todo
                    break;
                case "4": //delete transaction
                    //..todo
                    break;
                case "5": //view account details
                    //..todo
                    break;
                case "6": //logout
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