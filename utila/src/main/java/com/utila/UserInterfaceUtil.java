package com.utila;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;


public class UserInterfaceUtil {
    @RequiresApi(api = Build.VERSION_CODES.M)
    static void showPermissionInfo(Context context, String title, String body, final Activity activity, final String permission) {
        new MaterialDialog.Builder(context)
                .title(title)
                .content(body)
                .positiveText("ALLOW")
                .negativeText("DENY")
                .onPositive((dialog, which) -> {
                    SharedPreferences.Editor editor = PreferenceUtil.getPreferenceEditor(context);
                    if (PreferenceUtil.getSharedPreference(context).contains("PERMISSION")) {
                        String permissionString = PreferenceUtil.getSharedPreference(context).getString("PERMISSION", "");
                        if (permissionString.contains(permission)) {
                            dialog.dismiss();
                            openAppSettings(context);
                        } else {
                            editor.putString("PERMISSION", permissionString + "," + permission);
                            editor.apply();
                            dialog.dismiss();
                            activity.requestPermissions(new String[]{permission}, 123);
                        }
                    } else {
                        editor.putString("PERMISSION", permission);
                        editor.apply();
                        dialog.dismiss();
                        activity.requestPermissions(new String[]{permission}, 123);
                    }
                })
                .onNegative((dialog, which) -> dialog.dismiss())
                .show();
    }

    public static void showSnackBar(Context context, CoordinatorLayout coordinatorLayout, String message, boolean action, String buttonText,
                                    int snackBarLength, int actionColor, SnackBarAction snackBarAction) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, snackBarLength);
        if (action) {
            snackbar.setActionTextColor(ResourceUtil.getColor(context, actionColor));
            snackbar.setAction(buttonText, view -> snackBarAction.action());
        }
        snackbar.show();
    }

    public static MaterialDialog runProgressDialog(Context context, String title, String body) {
        final MaterialDialog progressDialog = new MaterialDialog.Builder(context)
                .title(title)
                .content(body)
                .progress(true, 0)
                .show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        return progressDialog;
    }

    public static void showInfoDialog(Context context, String title, String body, String positiveText, String negativeText, boolean autoDismiss, boolean cancelable,
                                      @Nullable MyDialogAction myDialogAction) {
        MaterialDialog.Builder materialDialog = new MaterialDialog.Builder(context)
                .title(title)
                .content(body)
                .autoDismiss(autoDismiss)
                .cancelable(cancelable);

        if (EmptyUtil.isNotNull(positiveText) && EmptyUtil.isNotEmpty(positiveText)) {
            materialDialog.positiveText(positiveText);
            if (EmptyUtil.isNotNull(myDialogAction)) {
                materialDialog.onPositive((materialDialog1, dialogAction) -> myDialogAction.onOk(materialDialog1));
            } else {
                materialDialog.onPositive((materialDialog1, dialogAction) -> materialDialog1.dismiss());
            }
        }

        if (EmptyUtil.isNotNull(negativeText) && EmptyUtil.isNotEmpty(negativeText)) {
            materialDialog.negativeText(negativeText);
            if (EmptyUtil.isNotNull(myDialogAction)) {
                materialDialog.onNegative((materialDialog1, dialogAction) -> myDialogAction.onCancel(materialDialog1));
            } else {
                materialDialog.onNegative((materialDialog1, dialogAction) -> materialDialog1.dismiss());
            }
        }

        materialDialog.show();
    }


    public static void showToast(Context context, String message, int duration) {
        Toast.makeText(context, message, duration).show();
    }

    private static void openAppSettings(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }

    /*Interfaces*/
    public interface MyDialogAction {
        void onOk(MaterialDialog materialDialog);

        void onCancel(MaterialDialog materialDialog);
    }

    public interface SnackBarAction {
        void action();
    }
}
