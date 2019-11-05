package com.khalti.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import com.tbruyelle.rxpermissions.RxPermissions;


public class AppPermissionUtil {
    public static boolean checkAndroidPermission(Context context, String permission) {
        int res = context.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public static void askPermission(Activity activity, String permission, String messageBody, MyPermission myPermission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            RxPermissions rxPermissions = new RxPermissions(activity);
            rxPermissions.requestEach(permission)
                    .subscribe(permission1 -> {
                        if (permission1.shouldShowRequestPermissionRationale) {
                            UserInterfaceUtil.INSTANCE.showPermissionInfo(activity, "Grant permission", messageBody, activity, permission);
                        } else if (permission1.granted) {
                            myPermission.onPermission();
                        }
                    });
        } else {
            myPermission.onPermission();
        }
    }

    public interface MyPermission {
        void onPermission();
    }
}
