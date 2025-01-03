package com.expense_tracker;

import java.util.Date;

public class Transaction {

    private double amount;
    private String description;
    private String category;
    private User user;

    private int id;

    private boolean isIncome;

    private Date date;

    public Transaction(int id, double amount, User user) {
        this(id, amount, "", "Miscellaneous", user, false, null);
    }

    public Transaction(int id, double amount, String description, String category, User user, boolean isIncome, Date date) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.category = category;
        this.user = user;
        this.isIncome = isIncome;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isIncome() {
        return isIncome;
    }

    public void setIncome(boolean income) {
        isIncome = income;
    }
}
