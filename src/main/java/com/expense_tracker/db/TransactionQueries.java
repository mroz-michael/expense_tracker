package com.expense_tracker.db;

public class TransactionQueries {


    public static String create(String tableName) {
        String query = "Insert into " + tableName +
                "(amount, description, category, user_id, date) " +
                "VALUES(?, ?, ?, ?, ?);";
        return query;
    }
    public static String getOne(String tableName) {
            String query = "SELECT id, description, amount, date, user_id, category from "
            + tableName + " where id = ? ";
            return query;
    }

    public static String getAll(String tableName) {
        String query = "SELECT id, description, amount, date, user_id, category from "
                + tableName + " where user_id = ? ";
        return query;
    }

    public static String getByAmount(String tableName) {
        String query = "SELECT id, description, amount, date, user_id, category from "
                + tableName + " where user_id = ? and amount between ? and ?";
        return query;
    }

    public static String getByCategory(String tableName) {
        String query = "SELECT id, description, amount, date, user_id, category from "
                + tableName + " where user_id = ? and category = ?";
        return query;
    }

    public static String getByDate(String tableName) {
        String query = "SELECT id, description, amount, date, user_id, category from "
                + tableName + " where user_id = ? and date between ? and ?";
        return query;
    }

    public static String update(String tableName) {
        String query = "update " + tableName + " " + "set amount = ?" +
                ", description = ?, category = ? where id= ?";
        return query;
    }

    public static String delete(String tableName) {
        String query = "delete from " + tableName + " where id = ? ";
        return query;
    }
}
