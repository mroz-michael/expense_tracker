package com.expense_tracker.services;

import com.expense_tracker.db.UserQueryExecutor;
import org.mindrot.jbcrypt.BCrypt;
import com.expense_tracker.User;

import java.util.Scanner;

public class AuthenticationService {

    public static User login(String username, String plainTextPw, boolean isTest) {

        if (validatePassword(username, plainTextPw, isTest)) {
            return UserQueryExecutor.findUserByName(username, isTest);
        } else {
            System.out.println("Invalid username or password.");
            return null;
        }
    }

    public static User createUser(String username, String unHashedPw, boolean isTest) {
        String pwHash = generateHash(unHashedPw);
        User newUser = UserQueryExecutor.createUser(username, pwHash, "user", isTest);
        return newUser;
    }

    public static String generateHash(String plaintext) {
        return BCrypt.hashpw(plaintext, BCrypt.gensalt());
    }

    public static boolean validatePassword(String username, String plaintextUserPassword, boolean isTest) {
        User foundUser = UserQueryExecutor.findUserByName(username, isTest);
        return foundUser == null ? false : BCrypt.checkpw(plaintextUserPassword, foundUser.getPwHash());
    }
}

