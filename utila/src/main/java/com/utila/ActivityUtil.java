package com.utila;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.HashMap;

public class ActivityUtil {

    public static void openActivity(Class className, Context context, boolean hasData, HashMap<String, ?> data, boolean animate) {
        Intent intent = new Intent(context, className);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (animate) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        }

        if (hasData) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("map", data);
            intent.putExtra("bundle", bundle);
        }

        context.startActivity(intent);
    }
}
