package com.utila;

import android.app.Activity;
import android.util.DisplayMetrics;

import java.util.HashMap;

public class DeviceUtil {

    public static HashMap<String, Integer> getDeviceSize(Activity activity) {
        HashMap<String, Integer> sizeMap = new HashMap<>();
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int height = metrics.heightPixels;
        int width = metrics.widthPixels;

        sizeMap.put("width", width);
        sizeMap.put("height", height);

        return sizeMap;
    }

    public static Integer getDeviceWidth(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        return metrics.widthPixels;
    }

    public static Integer getDeviceHeight(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        return metrics.heightPixels;
    }
}
