package com.khalti.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

public class ResourceUtil {
    public static String getString(Context context, int id) {
        return context.getResources().getString(id);
    }

    public static int getColor(Context context, int id) {
        return ContextCompat.getColor(context, id);
    }
    public static Drawable getDrawable(@NonNull Context context, @NonNull Integer id) {
        return AppCompatResources.getDrawable(context, id);
    }

}
