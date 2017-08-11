package com.utila;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.HashMap;

public class ActivityUtil {

    private static Intent intent;

    public static void openActivity(Class className, Context context, boolean hasData, HashMap<String, ?> data, boolean animate) {
        intent = new Intent(context, className);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (animate) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        }

        if (hasData) {
            Bundle bundle = new Bundle();
            intent.putExtra("map", data);
        }

        context.startActivity(intent);
    }

    public static void openActivityForResult(Class className, Activity activity, HashMap<String, Object> data, Integer requestId) {
        intent = new Intent(activity.getApplicationContext(), className);
        if (EmptyUtil.isNotNull(data) && data.size() > 0) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("map", data);
            intent.putExtras(bundle);
        }

        activity.startActivityForResult(intent, requestId);
    }
}
