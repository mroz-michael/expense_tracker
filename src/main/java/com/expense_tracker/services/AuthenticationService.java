package com.expense_tracker.services;

import org.mindrot.jbcrypt.BCrypt;
import com.expense_tracker.User;

public class AuthenticationService {

    public User login(String[] credentials) {
        String pw = credentials[1];
        //call QueryExecutor to get User from db based on username and then compare
        User tempPlaceholder = new User("aaaa", false);
        if (passwordValid(tempPlaceholder, pw)) {
            return new User("this will be a user object returned by the QueryExecutor", false);
        } else {
            System.out.println("Invalid username or password.");
            return null;
        }
    }
    private static String generateHash(String plaintext) {
        return BCrypt.hashpw(plaintext, BCrypt.gensalt());
    }

    public static boolean passwordValid(User user, String plaintextPassword) {
        //call queryExecutor to get user's pw hash, compare with hashed plaintextpw
        String pwHash = generateHash(plaintextPassword);
        return BCrypt.checkpw(pwHash, user.getPwHash());
    }
}

