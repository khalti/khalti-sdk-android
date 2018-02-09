package khalti.checkOut.Wallet;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.HashMap;

import khalti.R;
import khalti.SmsListener;
import khalti.carbonX.widget.Button;
import khalti.carbonX.widget.FrameLayout;
import khalti.rxBus.Event;
import khalti.rxBus.RxBus;
import khalti.utils.AppPermissionUtil;
import khalti.utils.EmptyUtil;
import khalti.utils.ExpandableLayout;
import khalti.utils.NetworkUtil;
import khalti.utils.NumberUtil;
import khalti.utils.ResourceUtil;
import khalti.utils.Store;
import khalti.utils.UserInterfaceUtil;
import rx.Observable;
import rx.subscriptions.CompositeSubscription;


public class Wallet extends Fragment implements khalti.checkOut.Wallet.WalletContract.View {

    private EditText etMobile, etCode, etPIN;
    private khalti.carbonX.widget.TextInputLayout tilMobile, tilCode, tilPIN;
    private ExpandableLayout elConfirmation;
    private Button btnPay;
    private Dialog progressDialog;
    private LinearLayout llConfirmation, llPIN, llCode;

    private FragmentActivity fragmentActivity;
    private WalletContract.Presenter presenter;
    private CompositeSubscription compositeSubscription;
    private SmsListener smsListener;

    private boolean isRegistered = false;
    private int height = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

        presenter.setUpLayout();

        compositeSubscription = new CompositeSubscription();
        compositeSubscription.add(RxBus.getInstance().register(Event.class, event -> {
            if (event.getTag().equals("wallet_code")) {
                presenter.setConfirmationCode(event);
            }
        }));

        return mainView;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EmptyUtil.isNotNull(compositeSubscription) && !compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
        presenter.toggleSmsListener(false);
    }

    @Override
    public HashMap<String, Observable<CharSequence>> setEditTextListener() {
        return new HashMap<String, Observable<CharSequence>>() {{
            put("mobile", RxTextView.textChanges(etMobile));
            put("code", RxTextView.textChanges(etCode));
            put("pin", RxTextView.textChanges(etPIN));
        }};

        /*etMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tilMobile.setErrorEnabled(false);
                if (btnPay.getText().toString().toLowerCase().contains("confirm")) {
                    listener.toggleConfirmationLayout(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (EmptyUtil.isNotNull(tilCode.getError())) {
                    llCode.measure(0, 0);
                    int beforeA = llCode.getMeasuredHeight();

                    tilCode.setErrorEnabled(false);

                    llCode.measure(0, 0);
                    int afterA = llCode.getMeasuredHeight();

                    height = Math.abs(height + (beforeA - afterA));

                    ExpandableLayout.LayoutParams layoutParams = (ExpandableLayout.LayoutParams) llConfirmation.getLayoutParams();
                    layoutParams.height = llConfirmation.getHeight() - height;
                    llConfirmation.setLayoutParams(layoutParams);

                    height = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etPIN.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (EmptyUtil.isNotNull(tilPIN.getError())) {
                    llPIN.measure(0, 0);
                    int beforeA = llPIN.getMeasuredHeight();

                    tilPIN.setErrorEnabled(false);

                    llPIN.measure(0, 0);
                    int afterA = llPIN.getMeasuredHeight();

                    height = Math.abs(height + (beforeA - afterA));

                    ExpandableLayout.LayoutParams layoutParams = (ExpandableLayout.LayoutParams) llConfirmation.getLayoutParams();
                    layoutParams.height = llConfirmation.getHeight() - height;
                    llConfirmation.setLayoutParams(layoutParams);

                    height = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/
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
                presenter.unSubscribe();
            });
        } else {
            if (EmptyUtil.isNotNull(progressDialog)) {
                progressDialog.dismiss();
            }
        }
    }

    @Override
    public void setEditTextError(String view, String error) {
        switch (view) {
            case "mobile":
                tilMobile.setError(error);
                break;
            case "code":
                llCode.measure(0, 0);
                int beforeA = llCode.getMeasuredHeight();

                tilCode.setError(error);

                llCode.measure(0, 0);
                int afterA = llCode.getMeasuredHeight();

                height = Math.abs(height + (afterA - beforeA));
                break;
            case "pin":
                llPIN.measure(0, 0);
                int beforeB = llPIN.getMeasuredHeight();

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

        /*btnPay.setOnClickListener(view -> {
            if (btnPay.getText().toString().toLowerCase().contains("confirm")) {
                if (listener.isFinalFormValid(etPIN.getText().toString(), etCode.getText().toString())) {
                    listener.confirmPayment(NetworkUtil.isNetworkAvailable(fragmentActivity), etCode.getText().toString(), etPIN.getText().toString());
                } else {
                    listener.updateConfirmationHeight();
                }
            } else {
                if (listener.isMobileValid(etMobile.getText().toString())) {
                    if (AppPermissionUtil.checkAndroidPermission(fragmentActivity, Manifest.permission.RECEIVE_SMS)) {
                        listener.initiatePayment(NetworkUtil.isNetworkAvailable(fragmentActivity), etMobile.getText().toString());
                    } else {
                        AppPermissionUtil.askPermission(fragmentActivity, Manifest.permission.RECEIVE_SMS, "Please allow permission to receive SMS", () ->
                                listener.initiatePayment(NetworkUtil.isNetworkAvailable(fragmentActivity), etMobile.getText().toString()));
                    }
                }
            }
        });*/
    }

    @Override
    public void setConfirmationCode(String code) {
        etCode.setText(code);
        etPIN.requestFocus();
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
    public boolean hasContactPermission() {
        return AppPermissionUtil.checkAndroidPermission(fragmentActivity, Manifest.permission.RECEIVE_SMS);
    }

    @Override
    public void askContactPermission() {
        AppPermissionUtil.askPermission(fragmentActivity, Manifest.permission.RECEIVE_SMS, "Please allow permission to receive SMS", () -> presenter.onSmsReceiptPermitted());
    }

    @Override
    public boolean hasNetwork() {
        return NetworkUtil.isNetworkAvailable(fragmentActivity);
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
    public void setPresenter(WalletContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
