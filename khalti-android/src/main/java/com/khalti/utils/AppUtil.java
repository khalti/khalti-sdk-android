package com.khalti.utils;

import android.os.Build;

public class AppUtil {

    public static String getOsVersion() {
        return Build.VERSION.RELEASE;
    }

    public static Integer getApiLevel() {
        return Build.VERSION.SDK_INT;
    }
}
