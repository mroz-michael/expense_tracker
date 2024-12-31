package com.expense_tracker;
public class User {

        private String username;
        private String userType;
        private String pwHash;


        public User(String username) {
            this.username = username;
            this.userType = "user";
        }

        public String getUsername() {
            return this.username;
        }

        public String getUserType() {
            return this.userType;
        }

        public void changePassword(String newPassword) {
            //prompt enter curr pw, call Authenticator to check and then hash the new pw and set it

        }

        public User deleteUser(String usernameToDelete) {
            if (!getUserType().equals("admin")) {
                System.out.println(getUsername() + " does not have authorization to perform this operation.");
                return null;
            }
            //call User service to remove user
            return null;
        }

}
