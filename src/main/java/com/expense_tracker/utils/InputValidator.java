package com.expense_tracker.utils;


import com.expense_tracker.User;
import com.expense_tracker.controllers.UserInterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.zip.DataFormatException;

public class InputValidator {

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy MM dd");

    public static boolean validDouble(String inputDouble) {

        if (inputDouble.isEmpty()) { return false;}
        boolean isNegative = inputDouble.charAt(0) == '-';
        boolean leadingDecimal = inputDouble.charAt(0) == '.';
        boolean seenDecimal = leadingDecimal; //flag that at most only 1 '.' appears
        int startingIndex = isNegative | leadingDecimal ? 1 : 0;
        char[] inputArr = inputDouble.toCharArray();

        for (int i = startingIndex; i < inputArr.length; i++) {
            char curr = inputArr[i];
            if (curr == '.' && seenDecimal) {
                return  false;
            }

            else if (curr == '.') {
                seenDecimal = true;
                continue;
            }

            if (! (curr >= '0' && curr <= '9') ) {
                return  false;
            }
        }

        return true;
    }

    //required format: year-month-day
    public  static boolean validDate(String dateInput) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy MM dd");

        try {
            formatter.parse(dateInput);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean validInt(String numInput, int minAllowed, int maxAllowed) {

        for (char c: numInput.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }

        int num = Integer.valueOf(numInput);
        return num >= minAllowed && num <= maxAllowed;
    }

    /**
     * called to check if user inputted command exists as a valid key in UserInterface.COMMAND_LIST Map
     * @param command User's inputted command when prompted by UserInterface.displayMenu()
     * @return true if and only if command is a number in range [1, COMMAND_LIST.size()]
     */
    public static boolean validCommand(String command) {

        int numCommands = UserInterface.numCommands();

        try {
            int commandNum = Integer.parseInt(command.trim());
            return commandNum > 0 && commandNum <= numCommands;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * validate user's transactions search filter
     * @param query the filter condition to place on querying the DB for user's transactions
     * @return true if and only if query is 'all' or 'date' or 'amount' or 'category'
     */
    public static boolean validateQuery(String query) {

        for (String validInput: UserInterface.getValidQueries()) {
            if (query.toLowerCase().trim().equals(validInput)) {
                return true;
            }
        }
        return false;
    }
}
