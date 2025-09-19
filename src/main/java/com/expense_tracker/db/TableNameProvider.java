package com.expense_tracker.db;

public class TableNameProvider {

    private String tableSuffix;
    /*
     * constructor - Determine if table name contains "_test" suffix based on given environment
     */
    public TableNameProvider(String tableSuffix) {
        this.tableSuffix = tableSuffix;
    }
    /**
     * @return String the name of the database table for the user related query
     */
    public String provideUsersTable() {
        return "users" + this.tableSuffix;
    }

    public String provideTransactionsTable() {
        return "transactions" + this.tableSuffix;
    }
}
