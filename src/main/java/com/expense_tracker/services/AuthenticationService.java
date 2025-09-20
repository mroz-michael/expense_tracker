package com.expense_tracker.services;

import com.expense_tracker.db.UserQueryExecutor;
import org.mindrot.jbcrypt.BCrypt;
import com.expense_tracker.User;

public class AuthenticationService {

    private UserQueryExecutor userExecutor;

    public AuthenticationService(UserQueryExecutor userExecutor) {
        this.userExecutor = userExecutor;
    }

    public User login(String username, String plainTextPw) {

        if (validatePassword(username, plainTextPw)) {
            return userExecutor.findUserByName(username);
        } else {
            return null;
        }
    }

    public User createUser(String username, String unHashedPw) {
        String pwHash = generateHash(unHashedPw);
        String userType = firstUser() ? "admin" : "user";
        User newUser = userExecutor.createUser(username, pwHash, userType);
        return newUser;
    }

    /**
     * checks if there exists no users on the db, if so flag the next user as an admin
     * @return true if and only if the database contains 0 users
     */
    private boolean firstUser() {
        int numUsers = userExecutor.findNumUsers();
        return numUsers == 0;
    }

    public static String generateHash(String plaintext) {
        return BCrypt.hashpw(plaintext, BCrypt.gensalt());
    }

    public  boolean validatePassword(String username, String plaintextUserPassword) {
        User foundUser = userExecutor.findUserByName(username);
        return foundUser == null ? false : BCrypt.checkpw(plaintextUserPassword, foundUser.getPwHash());
    }
}

