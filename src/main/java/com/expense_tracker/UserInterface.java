package com.expense_tracker;

import com.expense_tracker.db.UserQueryExecutor;
import com.expense_tracker.services.AuthenticationService;

import java.util.Scanner;

public class UserInterface {

    public static User createUser(boolean isTest) {
        Scanner inputScan = new Scanner(System.in);
        System.out.println("Enter the username you'd like: ");
        String username = inputScan.next().trim();
        System.out.println("Please create a password for this account (use one you don't use for any other account)");
        String unHashedPw = inputScan.next().trim();
        String pwHash = AuthenticationService.generateHash(unHashedPw);
        User newUser = UserQueryExecutor.createUser(username, pwHash, "user", isTest);
        return newUser;
    }

    public static void promptPasswordChange(User user, String newPw, boolean isTest) {
        System.out.println("To change your password, first enter your current password.");
        Scanner inputScan = new Scanner(System.in);
        String oldPassword = inputScan.next();
        inputScan.close();
        boolean passwordIsValid = AuthenticationService.validatePassword(user.getUsername(), oldPassword, isTest);
        //call Authenticator with oldPassword. if authentic, get newPW, then send it to authenticator to hash/store

    }
}
