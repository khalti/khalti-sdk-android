package com.utila;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

public class ResourceUtil {
    public static String getString(Context context, int id) {
        return context.getResources().getString(id);
    }

    public static Drawable getDrawable(Context context, int id) {
        return ContextCompat.getDrawable(context, id);
    }

    public static int getColor(Context context, int id) {
        return ContextCompat.getColor(context, id);
    }
}
