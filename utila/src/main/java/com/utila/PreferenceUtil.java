package com.utila;

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

    public static String getPrefString(Context context, String key) {
        SharedPreferences sp = getSharedPreference(context);
        if (sp.contains(key)) {
            return sp.getString(key, "");
        } else {
            return "";
        }
    }
}
