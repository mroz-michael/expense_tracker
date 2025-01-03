package com.expense_tracker;

import java.util.Date;

public class Transaction {

    private double amount;
    private String description;
    private String category;
    private int userId;

    private int id;

    private boolean isIncome;

    private Date date;

    public Transaction(int id, double amount, int userId) {
        this(id, amount, "", "Miscellaneous", userId, false, null);
    }


    public Transaction(int id, double amount, String description, String category, int userId, boolean isIncome, Date date) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.category = category;
        this.userId = userId;
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

    public int getUserId() {
        return userId;
    }

    public void setUser(int userId) {
        this.userId = userId;
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
