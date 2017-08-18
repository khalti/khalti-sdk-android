package com.utila;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {

    private static boolean isValid(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        return matcher.find();
    }
}
