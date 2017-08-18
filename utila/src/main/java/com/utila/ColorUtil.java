package com.utila;

import android.graphics.Color;

public class ColorUtil {
    public static int getContrastColor(int color) {
        float[] hsv = new float[3];
        Color.RGBToHSV(Color.red(color), Color.green(color), Color.blue(color),
                hsv);
        if (hsv[2] < 0.65) {
            hsv[2] = 0.9f;
        } else {
            hsv[2] = 0.1f;
        }
        hsv[1] = hsv[1] * 0.2f;
        return Color.HSVToColor(hsv);
    }
}
