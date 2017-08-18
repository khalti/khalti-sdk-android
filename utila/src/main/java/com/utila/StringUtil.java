package com.utila;

import java.util.List;

public class StringUtil {
    public static String getNameIcon(String name) {
        String[] splitName = name.split("\\s+");
        String icon;
        if (splitName.length > 1) {
            String iconName = splitName[0].substring(0, 1) + splitName[splitName.length - 1].substring(0, 1);
            icon = (iconName.toUpperCase());
        } else {
            icon = splitName[0].substring(0, 1).toUpperCase();
        }
        return icon;
    }

    public static String formatNumber(Double number) {
        return String.format("%.2f", number);
    }

    public static String getFormattedHint(String hint) {
        return "<small><small>" + hint + "</small></small>";
    }

    public static String replaceInUrl(String fullUrl, String text) {
        return fullUrl.replaceAll(":.*?/", text + "/");
    }

    public static String addCommas(String digits) {

        String result = digits;

        if (digits.length() <= 3) return digits;

        for (int i = 0; i < (digits.length() - 1) / 3; i++) {

            int commaPos = digits.length() - 3 - (3 * i);

            result = result.substring(0, commaPos) + "," + result.substring(commaPos);
        }
        return result;
    }

    public static String addDots(String s) {
        String result = s;
        int length = s.length();
        if (length <= 3) return s;
        if (length > 3 && length <= 5 && !String.valueOf(result.charAt(3)).equalsIgnoreCase(".")) {
            result = result.substring(0, 3) + "." + result.substring(3, length);
        } else if (length > 6 && !String.valueOf(result.charAt(6)).equalsIgnoreCase(".")) {
            if (!String.valueOf(result.charAt(3)).equalsIgnoreCase(".")) {
                result = result.substring(0, 3) + "." + result.substring(3, length);
            }
            result = result.substring(0, 6) + "." + result.substring(6, length);
        }
        return result;
    }


    public static boolean exactContains(String fullString, String containingString) {
        return fullString.contains(containingString);
    }

    public static boolean contains(String fullString, String containingString) {
        return fullString.toLowerCase().contains(containingString.toLowerCase());
    }

    public static String convertToCamelCase(String s) {
        String x = s.substring(0, 1);
        String y = s.substring(1, s.length());
        return x.toUpperCase() + y.toLowerCase();
    }

    public static String createCommaSeparateValuesFromList(List<String> data) {
        String formattedString = "";
        for (String value : data) {
            formattedString = formattedString + value + ", ";
        }
        if (formattedString.contains(",")) {
            return formattedString.substring(0, (formattedString.length() - 2));
        } else {
            return formattedString;
        }
    }

    public static String getPsuedoUniqueId() {
        String time = System.currentTimeMillis() + "";
        String[] s = (time.replaceAll(".....(?!$)", "$0 ")).split("\\s+");
        Integer a = 0;
        String id = "";
        for (int i = 0; i < s[0].length(); i++) {
            a += Integer.parseInt(s[0].charAt(i) + "");
        }
        for (int i = 1; i < s.length; i++) {
            id += s[i];
        }
        return id;
    }
}
