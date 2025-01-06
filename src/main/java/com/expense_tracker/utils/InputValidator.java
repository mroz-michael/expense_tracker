package com.expense_tracker.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.zip.DataFormatException;

public class InputValidator {

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
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
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
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
}
