package khalti.checkOut.Wallet;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.HashMap;

import khalti.R;
import khalti.SmsListener;
import khalti.carbonX.widget.Button;
import khalti.carbonX.widget.FrameLayout;
import khalti.utils.AppPermissionUtil;
import khalti.utils.EmptyUtil;
import khalti.utils.ExpandableLayout;
import khalti.utils.NetworkUtil;
import khalti.utils.NumberUtil;
import khalti.utils.ResourceUtil;
import khalti.utils.Store;
import khalti.utils.UserInterfaceUtil;
import rx.Observable;

public class Wallet extends Fragment implements khalti.checkOut.Wallet.WalletContract.View {

    private EditText etMobile, etCode, etPIN;
    private khalti.carbonX.widget.TextInputLayout tilMobile, tilCode, tilPIN;
    private ExpandableLayout elConfirmation;
    private Button btnPay;
    private Dialog progressDialog;
    private LinearLayout llConfirmation, llPIN, llCode, llKhaltiBranding;
    private ImageView ivKhalti;
    private CardView cvPinMessage;
    private AppCompatTextView tvPinMessage;

    private FragmentActivity fragmentActivity;
    private WalletContract.Presenter presenter;
    private SmsListener smsListener;

    private boolean isRegistered = false;
    private int height = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.wallet_form, container, false);
        fragmentActivity = getActivity();
        presenter = new WalletPresenter(this);

        etMobile = mainView.findViewById(R.id.etMobile);
        etCode = mainView.findViewById(R.id.etCode);
        etPIN = mainView.findViewById(R.id.etPIN);
        tilMobile = mainView.findViewById(R.id.tilMobile);
        tilCode = mainView.findViewById(R.id.tilCode);
        tilPIN = mainView.findViewById(R.id.tilPIN);
        btnPay = mainView.findViewById(R.id.btnPay);
        elConfirmation = mainView.findViewById(R.id.elConfirmation);
        llConfirmation = mainView.findViewById(R.id.llConfirmation);
        llPIN = mainView.findViewById(R.id.llPIN);
        llCode = mainView.findViewById(R.id.llCode);
        ivKhalti = mainView.findViewById(R.id.ivKhalti);
        llKhaltiBranding = mainView.findViewById(R.id.llKhaltiBranding);
        cvPinMessage = mainView.findViewById(R.id.cvPinMessage);
        tvPinMessage = mainView.findViewById(R.id.tvPinMessage);

        presenter.onCreate();

        return mainView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public HashMap<String, Observable<CharSequence>> setEditTextListener() {
        return new HashMap<String, Observable<CharSequence>>() {{
            put("mobile", RxTextView.textChanges(etMobile));
            put("code", RxTextView.textChanges(etCode));
            put("pin", RxTextView.textChanges(etPIN));
        }};
    }

    @Override
    public void toggleProgressDialog(String action, boolean show) {
        FrameLayout flCircularProgress = (FrameLayout) fragmentActivity.getLayoutInflater().inflate(R.layout.component_circular_progress, null);
        String message = "";
        switch (action) {
            case "init":
                message = ResourceUtil.getString(fragmentActivity, R.string.init_payment);
                break;
            case "confirm":
                message = ResourceUtil.getString(fragmentActivity, R.string.confirming_payment);
                break;
        }

        if (show) {
            progressDialog = UserInterfaceUtil.runProgressDialog(fragmentActivity, message, ResourceUtil.getString(fragmentActivity, R.string.please_wait), flCircularProgress, () -> {
//                presenter.unSubscribe();
            });
        } else {
            if (EmptyUtil.isNotNull(progressDialog)) {
                progressDialog.dismiss();
            }
        }
    }

    @Override
    public void setEditTextError(String view, String error) {
        boolean isError = EmptyUtil.isNotNull(error);
        switch (view) {
            case "mobile":
                tilMobile.setErrorEnabled(isError);
                tilMobile.setError(error);
                break;
            case "code":
                llCode.measure(0, 0);
                int beforeA = llCode.getMeasuredHeight();

                tilCode.setErrorEnabled(isError);
                tilCode.setError(error);

                llCode.measure(0, 0);
                int afterA = llCode.getMeasuredHeight();

                height = Math.abs(height + (afterA - beforeA));
                break;
            case "pin":
                llPIN.measure(0, 0);
                int beforeB = llPIN.getMeasuredHeight();

                tilPIN.setErrorEnabled(isError);
                tilPIN.setError(error);

                llPIN.measure(0, 0);
                int afterB = llPIN.getMeasuredHeight();

                height = Math.abs(height + (afterB - beforeB));
                break;
        }
    }

    @Override
    public void setButtonText(String text) {
        btnPay.setText(text);
    }

    @Override
    public Observable<Void> setButtonClickListener() {
        return RxView.clicks(btnPay);
    }

    @Override
    public Observable<Void> setImageClickListener() {
        return RxView.clicks(ivKhalti);
    }

    @Override
    public void setConfirmationCode(String code) {
        etCode.setText(code);
        etPIN.requestFocus();
    }

    @Override
    public void setConfirmationLayoutHeight(String view) {
        TextInputLayout til;
        LinearLayout ll;
        if (view.equals("code")) {
            til = tilCode;
            ll = llCode;
        } else {
            til = tilPIN;
            ll = llPIN;
        }
        if (EmptyUtil.isNotNull(til.getError())) {
            ll.measure(0, 0);
            int beforeA = ll.getMeasuredHeight();

            til.setErrorEnabled(false);

            ll.measure(0, 0);
            int afterA = ll.getMeasuredHeight();

            height = Math.abs(height + (beforeA - afterA));

            ExpandableLayout.LayoutParams layoutParams = (ExpandableLayout.LayoutParams) llConfirmation.getLayoutParams();
            layoutParams.height = llConfirmation.getHeight() - height;
            llConfirmation.setLayoutParams(layoutParams);

            height = 0;
        }
    }

    @Override
    public void showNetworkError() {
        UserInterfaceUtil.showSnackBar(fragmentActivity, ((khalti.checkOut.CheckOutActivity) this.fragmentActivity).cdlMain, ResourceUtil.getString(fragmentActivity, R.string.network_error_body),
                false, "", 0, 0, null);
    }

    @Override
    public void showMessageDialog(String title, String message) {
        UserInterfaceUtil.showInfoDialog(fragmentActivity, title, message, true, true, ResourceUtil.getString(fragmentActivity, R.string.got_it), null,
                new UserInterfaceUtil.DialogAction() {
                    @Override
                    public void onPositiveAction(Dialog dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onNegativeAction(Dialog dialog) {

                    }
                });
    }

    @Override
    public void showPINDialog(String title, String message) {
        UserInterfaceUtil.showInfoDialog(fragmentActivity, title, message, true, true, ResourceUtil.getString(fragmentActivity, R.string.ok),
                ResourceUtil.getString(fragmentActivity, R.string.cancel), new UserInterfaceUtil.DialogAction() {
                    @Override
                    public void onPositiveAction(Dialog dialog) {
                        dialog.dismiss();
                        presenter.openKhaltiSettings();
                    }

                    @Override
                    public void onNegativeAction(Dialog dialog) {
                        dialog.dismiss();
                    }
                });
    }

    @Override
    public void showPINInBrowserDialog(String title, String message) {
        UserInterfaceUtil.showInfoDialog(fragmentActivity, title, message, true, true, ResourceUtil.getString(fragmentActivity, R.string.ok),
                ResourceUtil.getString(fragmentActivity, R.string.cancel), new UserInterfaceUtil.DialogAction() {
                    @Override
                    public void onPositiveAction(Dialog dialog) {
                        dialog.dismiss();
                        presenter.openLinkInBrowser();
                    }

                    @Override
                    public void onNegativeAction(Dialog dialog) {
                        dialog.dismiss();
                    }
                });
    }

    @Override
    public void openKhaltiSettings() {
        Intent i;
        PackageManager manager = fragmentActivity.getPackageManager();
        try {
            i = manager.getLaunchIntentForPackage("com.khalti.red");
            i = EmptyUtil.isNotNull(i) ? i : manager.getLaunchIntentForPackage("com.khalti");
            if (EmptyUtil.isNull(i)) {
                throw new PackageManager.NameNotFoundException();
            }
            i.addCategory(Intent.CATEGORY_LAUNCHER);

            HashMap<String, String> map = new HashMap<>();
            map.put("pin_settings", "pin_settings");
            i.putExtra("map", map);

            startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {
            presenter.showPINInBrowserDialog("Error", ResourceUtil.getString(fragmentActivity, R.string.khalti_not_found) + "\n\n" +
                    ResourceUtil.getString(fragmentActivity, R.string.set_pin_in_browser));
        }
    }

    @Override
    public void openLinkInBrowser(String link) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(browserIntent);
    }

    @Override
    public void closeWidget() {
        fragmentActivity.finish();
    }

    @Override
    public void updateConfirmationHeight() {
        ExpandableLayout.LayoutParams layoutParams = (ExpandableLayout.LayoutParams) llConfirmation.getLayoutParams();
        layoutParams.height = llConfirmation.getHeight() + height;
        llConfirmation.setLayoutParams(layoutParams);

        height = 0;
    }

    @Override
    public String getMessage(String action) {
        switch (action) {
            case "pin_not_set":
                return ResourceUtil.getString(fragmentActivity, R.string.pin_not_set);
            case "pin_not_set_continue":
                return ResourceUtil.getString(fragmentActivity, R.string.pin_not_set_continue);
            default:
                return "";
        }
    }

    @Override
    public boolean hasSmsReceiptPermission() {
        return AppPermissionUtil.checkAndroidPermission(fragmentActivity, Manifest.permission.RECEIVE_SMS);
    }

    @Override
    public void askSmsReceiptPermission() {
        AppPermissionUtil.askPermission(fragmentActivity, Manifest.permission.RECEIVE_SMS, "Please allow permission to receive SMS", () -> presenter.onSmsReceiptPermitted());
    }

    @Override
    public boolean hasNetwork() {
        return NetworkUtil.isNetworkAvailable(fragmentActivity);
    }

    @Override
    public String getPayButtonText() {
        return btnPay.getText() + "";
    }

    @Override
    public HashMap<String, String> getFormData() {
        return new HashMap<String, String>() {{
            put("mobile", etMobile.getText() + "");
            put("code", etCode.getText() + "");
            put("pin", etPIN.getText() + "");
        }};
    }

    @Override
    public void showSlogan() {
        Toast.makeText(fragmentActivity, ResourceUtil.getString(fragmentActivity, R.string.slogan), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showBranding() {
        llKhaltiBranding.setVisibility(View.VISIBLE);
    }

    @Override
    public void toggleConfirmationLayout(boolean show) {
        String buttonText = show ? ResourceUtil.getString(fragmentActivity, R.string.confirm_payment) : "Pay Rs " + NumberUtil.convertToRupees(Store.getConfig().getAmount());
        btnPay.setText(buttonText);
        etCode.setText("");
        etPIN.setText("");
        elConfirmation.toggleExpansion();
    }

    @Override
    public void toggleSmsListener(boolean listen) {
        if (listen) {
            IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
            smsListener = new SmsListener();
            fragmentActivity.registerReceiver(smsListener, intentFilter);
            isRegistered = true;
        } else {
            if (EmptyUtil.isNotNull(smsListener) && isRegistered) {
                fragmentActivity.unregisterReceiver(smsListener);
                isRegistered = false;
            }
        }
    }

    @Override
    public void togglePinMessage(boolean show) {
        cvPinMessage.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setPinMessage(String message) {
        String s = message + " " + ResourceUtil.getString(fragmentActivity, R.string.sms_info);
        tvPinMessage.setText(s);
    }

    @Override
    public void setMobile(String mobile) {
        etMobile.setText(mobile);
        etMobile.setSelection(mobile.length());
    }

    @Override
    public void setPresenter(WalletContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
