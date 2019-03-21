package com.khalti.utils;

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
        String[] s = number.toString().split("\\.");
        if (Integer.parseInt(s[1]) > 0) {
            return number.toString();
        } else {
            return s[0];
        }
    }
}
