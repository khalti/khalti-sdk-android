package com.khalti.utils;

import com.khalti.form.api.Config;

public class DataHolder {
    private static Config config;

    public static Config getConfig() {
        return config;
    }

    public static void setConfig(Config config) {
        DataHolder.config = config;
    }
}
