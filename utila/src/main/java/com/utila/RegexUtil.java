package com.utila;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {

    private static boolean isValid(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        return matcher.find();
    }

    public static boolean isMobileNumberValid(String phoneNumber) {
        Pattern pattern = Pattern.compile("([9][678][0-9]{8})");
        Matcher matcher = pattern.matcher(phoneNumber);

        return matcher.find();
    }
}
