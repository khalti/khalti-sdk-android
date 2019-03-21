package com.khalti.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtil {
    public static boolean isMobileNumberValid(String phoneNumber) {
        Pattern pattern = Pattern.compile("([9][678][0-9]{8})");
        Matcher matcher = pattern.matcher(phoneNumber);

        return matcher.find();
    }
}
