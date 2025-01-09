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
            return null;
        }
    }

    public static User createUser(String username, String unHashedPw, boolean isTest) {
        String pwHash = generateHash(unHashedPw);
        String userType = firstUser(isTest) ? "admin" : "user";
        User newUser = UserQueryExecutor.createUser(username, pwHash, userType, isTest);
        return newUser;
    }

    /**
     * checks if there exists no users on the db, if so flag the next user as an admin
     * @return true if and only if the database contains 0 users
     */
    private static boolean firstUser(boolean isTest) {
        int numUsers = UserQueryExecutor.findNumUsers(isTest);
        return numUsers == 0;
    }

    public static String generateHash(String plaintext) {
        return BCrypt.hashpw(plaintext, BCrypt.gensalt());
    }

    public static boolean validatePassword(String username, String plaintextUserPassword, boolean isTest) {
        User foundUser = UserQueryExecutor.findUserByName(username, isTest);
        return foundUser == null ? false : BCrypt.checkpw(plaintextUserPassword, foundUser.getPwHash());
    }
}

