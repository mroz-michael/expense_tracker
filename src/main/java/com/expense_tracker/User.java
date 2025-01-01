package com.expense_tracker;

import java.util.Scanner;
public class User {

        private String username;
        private String userType;
        private String pwHash;


        public User(String username, boolean isAdmin) {
            this.username = username;
            this.userType = isAdmin ? "admin" : "user";
        }

        public String getUsername() {
            return this.username;
        }

    public String getPwHash() {
        return pwHash;
    }

    public String getUserType() {
            return this.userType;
        }

        public void changePassword(String newPassword) {
            //prompt enter curr pw, call Authenticator to check and then hash the new pw and set it
            System.out.println("To change your password, first enter your current password.");
            Scanner inputScan = new Scanner(System.in);
            String oldPassword = inputScan.next();
            inputScan.close();
            //call Authenticator with oldPassword. if authentic, get newPW, then send it to authenticator to hash/store

        }

        public void changeUsername(String newUsername) {
            System.out.print("Are you sure you want to change your username? This is a non-reversible action");
            System.out.println("Y/N");
            Scanner inputScan = new Scanner(System.in);
            String userAgrees = inputScan.next();
            if (userAgrees.toUpperCase().equals("Y") || userAgrees.toUpperCase().equals("YES")) {
                //send existing and new username to db to confirm uniqueness and update
                return;
            }
            System.out.println("Username change request cancelled by user.");
        }

        public User deleteUser(User userToDelete) {
            if (!getUserType().equals("admin")) {
                System.out.println(getUsername() + " does not have authorization to perform this operation.");
                return null;
            }
            //call User service to remove user
            return null;
        }

}
