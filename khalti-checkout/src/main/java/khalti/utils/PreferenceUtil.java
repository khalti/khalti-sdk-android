package khalti.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtil {
    private static SharedPreferences sharedPreference;

    public static SharedPreferences getSharedPreference(Context context) {
        if (EmptyUtil.isNull(sharedPreference)) {
            sharedPreference = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return sharedPreference;
    }

    public static SharedPreferences.Editor getPreferenceEditor(Context context) {
        if (EmptyUtil.isNotNull(sharedPreference)) {
            return sharedPreference.edit();
        } else {
            return getSharedPreference(context).edit();
        }
    }
}
