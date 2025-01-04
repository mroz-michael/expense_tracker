package com.expense_tracker.utils;

public class InputValidator {

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
            } else if (curr == '.') {
                seenDecimal = true;
                continue;
            }
            if (! (curr >= '0' && curr <= '9') ) {
                return  false;
            }
        }

        return true;
    }

    //required format: yyyy-mm-dd
    public  static boolean validDate(String dateInput) {

        if (dateInput.trim().length() != 10) {
            return false;
        }
        char[] inputArr = dateInput.toCharArray();

        if (! (inputArr[0] >= '1' && inputArr[0] <= '2') ) {
            return false;
        }

        for (int i = 1; i < 4; i++) {
            char curr = inputArr[i];
            if (! (curr >= '0' && curr <= '9') ) {
                return false;
            }
        }

        if (inputArr[4] == ' ') {
            inputArr[4] = '-';
        }

        if (inputArr[4] != '-') {
            return false;
        }
        //input[5] is start of month
        if (inputArr[5] != '0' || inputArr[5] != '1') {
            return false;
        }
        if (inputArr[5] == '0') {
            if (! (inputArr[6] >= '1' && inputArr[6] <= '9') ) {
                return false;
            }
        }
        else { //if mm = 1x, x must be 0, 1 or 2
            if (! (inputArr[6] >= '0' && inputArr[6] <= '2')) {
                return false;
            }
        }
        return true;
    }
}
