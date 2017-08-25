package com.utila;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

import fontana.RobotoMediumTextView;
import fontana.RobotoTextView;

public class UserInterfaceUtil {
    private static Dialog progressDialog;
    private static Dialog infoDialog;
    private static Dialog interactiveDialog;

    public static void showSnackBar(Context context, CoordinatorLayout coordinatorLayout, String message, boolean action, String buttonText,
                                    int snackBarLength, int actionColor, SnackBarAction snackBarAction) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, snackBarLength);
        if (action) {
            snackbar.setActionTextColor(ResourceUtil.getColor(context, actionColor));
            snackbar.setAction(buttonText, view -> snackBarAction.action());
        }
        snackbar.show();
    }

    public static Dialog runProgressDialog(Context context, String title, String body, View progressCircle, ProgressDialogAction progressDialogAction) {
        progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.progress_dialog);
        if (EmptyUtil.isNotNull(progressDialog.getWindow())) {
            progressDialog.getWindow().setLayout(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setCancelable(true);
        progressDialog.show();

        RobotoMediumTextView tvTitle = progressDialog.findViewById(R.id.tvTitle);
        RobotoTextView tvBody = progressDialog.findViewById(R.id.tvBody);
        FrameLayout flProgress = progressDialog.findViewById(R.id.flProgress);

        flProgress.addView(progressCircle);
        tvTitle.setText(title);
        tvBody.setText(body);

        progressDialog.setOnCancelListener(dialogInterface -> progressDialogAction.onDismiss());
        progressDialog.setOnDismissListener(dialogInterface -> progressDialogAction.onDismiss());

        return progressDialog;
    }

    public static void showInfoDialog(Context context, String title, String body, boolean autoDismiss, boolean cancelable, View positiveButton, DialogAction dialogAction) {
        infoDialog = new Dialog(context);
        infoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        infoDialog.setContentView(R.layout.info_dialog);
        if (EmptyUtil.isNotNull(infoDialog.getWindow())) {
            infoDialog.getWindow().setLayout(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        infoDialog.setCanceledOnTouchOutside(autoDismiss);
        infoDialog.setCancelable(cancelable);
        infoDialog.show();

        RobotoMediumTextView tvTitle = infoDialog.findViewById(R.id.tvTitle);
        RobotoTextView tvBody = infoDialog.findViewById(R.id.tvBody);
        FrameLayout flNegativeAction = infoDialog.findViewById(R.id.flNegativeAction);
        FrameLayout flPositiveAction = infoDialog.findViewById(R.id.flPositiveAction);

        flNegativeAction.setVisibility(View.GONE);
        flPositiveAction.addView(positiveButton);
        tvTitle.setText(title);
        tvBody.setText(body);

        positiveButton.setOnClickListener(view -> dialogAction.onPositiveAction(infoDialog));
    }

    public static void showInteractiveInfoDialog(Context context, String title, String body, boolean autoDismiss, boolean cancelable, View positiveButton, View negativeButton, DialogAction dialogAction) {
        interactiveDialog = new Dialog(context);
        interactiveDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        interactiveDialog.setContentView(R.layout.info_dialog);
        if (EmptyUtil.isNotNull(interactiveDialog.getWindow())) {
            interactiveDialog.getWindow().setLayout(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        interactiveDialog.setCanceledOnTouchOutside(autoDismiss);
        interactiveDialog.setCancelable(cancelable);
        interactiveDialog.show();

        RobotoMediumTextView tvTitle = interactiveDialog.findViewById(R.id.tvTitle);
        RobotoTextView tvBody = interactiveDialog.findViewById(R.id.tvBody);
        FrameLayout flNegativeAction = interactiveDialog.findViewById(R.id.flNegativeAction);
        FrameLayout flPositiveAction = interactiveDialog.findViewById(R.id.flPositiveAction);

        flPositiveAction.addView(positiveButton);
        flNegativeAction.addView(negativeButton);
        tvTitle.setText(title);
        tvBody.setText(body);

        positiveButton.setOnClickListener(view -> dialogAction.onPositiveAction(interactiveDialog));
        negativeButton.setOnClickListener(view -> dialogAction.onNegativeAction(interactiveDialog));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    static void showPermissionInfo(Context context, String title, String body, final Activity activity, View positiveButton, View negativeButton, final String permission) {
        showInteractiveInfoDialog(context, title, body, false, false, positiveButton, negativeButton, new DialogAction() {
            @Override
            public void onPositiveAction(Dialog dialog) {
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
            }

            @Override
            public void onNegativeAction(Dialog dialog) {
                dialog.dismiss();
            }
        });
    }

    public static void dismissAllDialogs() {
        if (EmptyUtil.isNotNull(progressDialog)) {
            progressDialog.dismiss();
        }

        if (EmptyUtil.isNotNull(infoDialog)) {
            infoDialog.dismiss();
        }

        if (EmptyUtil.isNotNull(interactiveDialog)) {
            interactiveDialog.dismiss();
        }
    }

    private static void openAppSettings(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }

    public interface DialogAction {
        void onPositiveAction(Dialog dialog);

        void onNegativeAction(Dialog dialog);
    }

    public interface ProgressDialogAction {
        void onDismiss();
    }

    public interface SnackBarAction {
        void action();
    }
}
