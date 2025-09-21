package com.expense_tracker.utils;


import com.expense_tracker.controllers.UserInterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

public class InputValidator {

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy MM dd");

    /**
     * validate user input such that it represents a legal double amount
     * @param inputDouble user input that will be treated as a double
     * @return true if and only if inputDouble is a valid double amount
     */
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

    /**
     * validate input according to date format yyyy MM dd
     * @param dateInput user input to represent a date
     * @return true if and only if dateInput is in the expected format
     */
    public static boolean validDate(String dateInput) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy MM dd");
        if (dateInput.replace(" ", "").length() < 8) {
            return false;
        }

        try {
            formatter.parse(dateInput);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * validate that user's input is an integer of an acceptable value
     * @param numInput user input that's intended to be a numeric integer value
     * @param minAllowed the minimum acceptable integer
     * @param maxAllowed the largest acceptable integer
     * @return true if and only if numInput is a valid int and is in the range [minAllowed, maxAllowed]
     */
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
     * @param command User's input after main menu prompt, already given trim() and toLowerCase()
     * @return true if and only if command is exit, 'exit', or a number in range [1, COMMAND_LIST.size()]
     */
    public static boolean validCommand(String command) {
        if (command.equals("exit") || command.equals("'exit'"))  {
            return true;
        }

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
