package com.expense_tracker.services;

import com.expense_tracker.db.UserQueryExecutor;
import org.mindrot.jbcrypt.BCrypt;
import com.expense_tracker.User;

public class AuthenticationService {

    public User login(String[] credentials, boolean isTest) {
        String pw = credentials[1];
        //call QueryExecutor to get User from db based on username and then compare
        if (validatePassword(credentials[0], pw, isTest)) {
            //get user from QE then compare
            return null;
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

