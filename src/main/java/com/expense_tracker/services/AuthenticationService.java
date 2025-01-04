package com.expense_tracker.services;

import com.expense_tracker.db.UserQueryExecutor;
import org.mindrot.jbcrypt.BCrypt;
import com.expense_tracker.User;

public class AuthenticationService {

    public static User login(String[] credentials, boolean isTest) {
        String username = credentials[0];
        String plainTextPw = credentials[1];
      
        if (validatePassword(username, plainTextPw, isTest)) {
            //get user from QE then compare
            return UserQueryExecutor.findUserByName(username, isTest);
        } else {
            System.out.println("Invalid username or password.");
            return null;
        }
    }
    public static String generateHash(String plaintext) {
        return BCrypt.hashpw(plaintext, BCrypt.gensalt());
    }

    public static boolean validatePassword(String username, String plaintextUserPassword, boolean isTest) {

        User foundUser = UserQueryExecutor.findUserByName(username, isTest);
        return foundUser == null ? false : BCrypt.checkpw(plaintextUserPassword, foundUser.getPwHash());
    }
}

