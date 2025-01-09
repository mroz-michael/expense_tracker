package com.expense_tracker;

import com.expense_tracker.controllers.UserInterface;
import com.expense_tracker.db.DBConnector;
import com.expense_tracker.utils.InputValidator;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main( String[] args ) {

        try {
            UserInterface.displayLoginScreen();
            String cmd = UserInterface.getLoginPrompt();
            if (cmd.equals("exit")) {return;}
            User currUser = cmd.equals("register") ? UserInterface.register() : UserInterface.login();
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
            //refer to UserInterface.COMMAND_LIST for mapping of numbers to commands
            switch (cmd) {
                case "1":
                    //..todo
                    break;
                case "2":
                    //..todo
                    break;
                case "3":
                    //..todo
                    break;
                case "4":
                    //..todo
                    break;
                case "5":
                    //..todo
                    break;
                case "6": //logout
                    return;
                case "7":
                    //..todo
                    break;
                case "8":
                    //..todo
                    break;
                case "9":
                    //todo
                    break;
                default: break;
            }
        }

    }
}