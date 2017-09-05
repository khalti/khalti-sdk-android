package khalti.utils;

import android.support.annotation.Keep;

import khalti.checkOut.api.Config;

@Keep
public class Store {
    private static Config config;

    public static Config getConfig() {
        return config;
    }

    public static void setConfig(Config config) {
        Store.config = config;
    }

}
