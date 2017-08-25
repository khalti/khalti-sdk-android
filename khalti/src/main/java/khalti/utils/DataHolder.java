package khalti.utils;

import khalti.checkOut.api.Config;

public class DataHolder {
    private static Config config;

    public static Config getConfig() {
        return config;
    }

    public static void setConfig(Config config) {
        DataHolder.config = config;
    }

}
