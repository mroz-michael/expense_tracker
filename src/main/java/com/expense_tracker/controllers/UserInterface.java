package com.expense_tracker.controllers;

import com.expense_tracker.User;
import com.expense_tracker.db.UserQueryExecutor;
import com.expense_tracker.services.AuthenticationService;

import java.util.Scanner;

public class UserInterface {

    private final static boolean NOT_TEST = false; //UserInterface not used for test methods


    public static void displayLoginScreen() {
        System.out.println("Welcome to the Expense Tracker Application\n");
        System.out.println("Please Enter One of the Following Commands to Login or Register: ");
        System.out.println("1: Login");
        System.out.println("2: Create a New Account");
    }

    public static void displayMainMenu() {
        System.out.println("Please Enter One of the Following Commands: ");
        int i = 0;
        System.out.println(++i +": Enter a Transaction");
        System.out.println(++i +": View Existing Transactions");
        System.out.println(++i +": Generate a Detailed Expense Report");
        System.out.println(++i +": Update an Existing Transaction");
        System.out.println(++i +": Delete an Existing Transaction");
        System.out.println(++i +": View Account Details");
        System.out.println(++i +": Logout");
        System.out.println(++i +": Change Username");
        System.out.println(++i +": Change Password");
        System.out.println(++i +": Delete Account");
    }
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
        String username = inputScan.next().trim();
        System.out.println("\nPlease create a password for this account");
        String unHashedPw = inputScan.next().trim();
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
}
