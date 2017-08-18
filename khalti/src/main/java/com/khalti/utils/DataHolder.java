package com.khalti.utils;

import com.khalti.form.api.Config;
import com.khalti.widget.basic.Pay;

public class DataHolder {
    private static Config config;
    private static Pay.OnSuccessListener onSuccessListener;

    public static Config getConfig() {
        return config;
    }

    public static void setConfig(Config config) {
        DataHolder.config = config;
    }

    public static Pay.OnSuccessListener getOnSuccessListener() {
        return onSuccessListener;
    }

    public static void setOnSuccessListener(Pay.OnSuccessListener onSuccessListener) {
        DataHolder.onSuccessListener = onSuccessListener;
    }
}
