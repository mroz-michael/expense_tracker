package com.expense_tracker;

import com.expense_tracker.controllers.UserInterface;

import java.util.Date;
public class User {

    private String username;
    private String userType;
    private String pwHash;
    private static int numUsers = 0;
    private int id;

    private Date dateJoined;

    public User() {
        this.id = numUsers++;
        this.username = "Default User";
        this.userType = "user";
        this.dateJoined = new Date();
    }

    public User(int id, String username, boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.userType = isAdmin ? "admin" : "user";
    }

    //constructor used when getting user from DB:
    public User(int id, String username, String pwHash, Date createdAt, String userRole) {
        this.id = id;
        this.username = username;
        this.pwHash = pwHash;
        this.dateJoined = createdAt;
        this.userType = userRole;
    }

    public String getUsername() {
        return this.username;
    }

    public int getId() {
        return id;
    }

    public Date getDateJoined() {
        return dateJoined;
    }

    public String getPwHash() {
        return pwHash;
    }

    public void setPwHash(String pwHash) {
        this.pwHash = pwHash;
    }

    public String getUserType() {
            return this.userType;
    }

    public void changePassword() {
        UserInterface.promptPasswordChange(this);
    }

    public void changeUsername() {
        UserInterface.promptUsernameChange(this);
    }

    //temp for testing
    public void setUsername(String username) {
        this.username = username;
    }

    //not currently in use. for future use when admin-specific menu commands are implemented
    public User deleteUser(User userToDelete) {
        if (! (getUserType().equals("admin") || getUserType().equals("owner")) ) {
            System.out.println(getUsername() + " does not have authorization to perform this operation.");
            return null;
        }
        //call User service to remove user
        return null;
    }

    @Override
    public String toString() {
        //only display user type if user is admin or owner
        String userTypeDisplayed = getUserType().equals("user") ? "" : "\nUser Type: " + getUserType();
        return "Username : " + getUsername() + userTypeDisplayed + "\nDate Joined: " + getDateJoined();
    }

}
