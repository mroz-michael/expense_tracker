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
            //todo: call appropriate method based on cmd value












        }

    }
}