package com.utila;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

import fontana.RobotoMediumTextView;
import fontana.RobotoTextView;

public class UserInterfaceUtil {
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
        Dialog progressDialog = new Dialog(context);
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
        Dialog infoDialog = new Dialog(context);
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

    public static void showInteractiveInfoDialog(Context context, String title, String body, boolean autoDismiss, boolean cancelable, View positiveButton, View negativeButton,
                                                 DialogAction dialogAction) {
        Dialog infoDialog = new Dialog(context);
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

        flPositiveAction.addView(positiveButton);
        flNegativeAction.addView(negativeButton);
        tvTitle.setText(title);
        tvBody.setText(body);

        positiveButton.setOnClickListener(view -> dialogAction.onPositiveAction(infoDialog));
        negativeButton.setOnClickListener(view -> dialogAction.onNegativeAction(infoDialog));
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
