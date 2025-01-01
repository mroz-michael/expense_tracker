package com.expense_tracker.services;

import com.expense_tracker.db.QueryExecutor;
import org.mindrot.jbcrypt.BCrypt;
import com.expense_tracker.User;

public class AuthenticationService {

    public User login(String[] credentials) {
        String pw = credentials[1];
        //call QueryExecutor to get User from db based on username and then compare
        if (validatePassword(credentials[0], pw)) {
            return new User("this will be a user object returned by the QueryExecutor", false);
        } else {
            System.out.println("Invalid username or password.");
            return null;
        }
    }
    public static String generateHash(String plaintext) {
        return BCrypt.hashpw(plaintext, BCrypt.gensalt());
    }

    public static boolean validatePassword(String username, String plaintextUserPassword) {
        //call queryExecutor to get user's pw hash, compare with plaintxtpw
        String existingPwHash = QueryExecutor.getUser(username).getPwHash();
        return BCrypt.checkpw(plaintextUserPassword, existingPwHash);
    }
}

