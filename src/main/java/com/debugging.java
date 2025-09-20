package com;
import java.sql.*;

import com.expense_tracker.db.TestDBConnector;

public class debugging {

    public static void main(String[] args) {
        try {
            Connection mysql = TestDBConnector.connect();
        } catch (Exception e) {
            System.out.println("oops: " + e.getMessage());
        }
    }

}
