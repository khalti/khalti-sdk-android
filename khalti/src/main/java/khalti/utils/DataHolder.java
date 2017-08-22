package khalti.utils;

import khalti.form.api.Config;
import khalti.widget.Button;

public class DataHolder {
    private static Config config;
    private static Button.OnSuccessListener onSuccessListener;

    public static Config getConfig() {
        return config;
    }

    public static void setConfig(Config config) {
        DataHolder.config = config;
    }

    public static Button.OnSuccessListener getOnSuccessListener() {
        return onSuccessListener;
    }

    public static void setOnSuccessListener(Button.OnSuccessListener onSuccessListener) {
        DataHolder.onSuccessListener = onSuccessListener;
    }
}
