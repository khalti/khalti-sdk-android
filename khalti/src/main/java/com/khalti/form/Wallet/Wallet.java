package com.khalti.form.Wallet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.khalti.R;
import com.khalti.carbonX.widget.Button;
import com.khalti.carbonX.widget.ExpandableLayout;
import com.khalti.carbonX.widget.TextInputLayout;
import com.khalti.utils.DataHolder;
import com.utila.NetworkUtil;
import com.utila.NumberUtil;
import com.utila.ResourceUtil;


public class Wallet extends Fragment implements WalletContract.View {

    private TextInputEditText etMobile, etCode, etPIN;
    private TextInputLayout tilMobile, tilCode, tilPIN;
    private ExpandableLayout elConfirmation;
    private Button btnPay;

    private FragmentActivity fragmentActivity;
    private WalletContract.Listener listener;

    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.payment_form, container, false);
        fragmentActivity = getActivity();
        listener = new WalletPresenter(this);

        etMobile = mainView.findViewById(R.id.etMobile);
        etCode = mainView.findViewById(R.id.etCode);
        etPIN = mainView.findViewById(R.id.etPIN);
        tilMobile = mainView.findViewById(R.id.tilMobile);
        tilCode = mainView.findViewById(R.id.tilCode);
        tilPIN = mainView.findViewById(R.id.tilPIN);
        btnPay = mainView.findViewById(R.id.btnPay);
        elConfirmation = mainView.findViewById(R.id.elConfirmation);

        listener.setUpLayout();

        return mainView;
    }

    @Override
    public void onPause() {
        super.onPause();
        listener.toggleEditTextListener(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        listener.toggleEditTextListener(true);
    }

    @Override
    public void toggleEditTextListener(boolean set) {
        if (set) {
            etMobile.addTextChangedListener(new TextWatcher() {
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
                    tilCode.setErrorEnabled(false);
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
                    tilPIN.setErrorEnabled(false);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        } else {
            etMobile.addTextChangedListener(null);
            etCode.addTextChangedListener(null);
            etPIN.addTextChangedListener(null);
        }
    }

    @Override
    public void setEditTextError(String view, String error) {
        switch (view) {
            case "mobile":
                tilMobile.setError(error);
                break;
            case "code":
                tilCode.setError(error);
                break;
            case "pin":
                tilPIN.setError(error);
                break;
        }
    }

    @Override
    public void setButtonText(String text) {
        btnPay.setText(text);
    }

    @Override
    public void setButtonClickListener() {
        btnPay.setOnClickListener(view -> {
            if (btnPay.getText().toString().toLowerCase().contains("confirm")) {

            } else {
                listener.continuePayment(NetworkUtil.isNetworkAvailable(fragmentActivity), etMobile.getText().toString());
            }
        });
    }

    @Override
    public void toggleConfirmationLayout(boolean show) {
        String buttonText = show ? ResourceUtil.getString(fragmentActivity, R.string.confirm_payment) : "Pay Rs " + NumberUtil.convertToRupees(DataHolder.getConfig().getAmount());
        btnPay.setText(buttonText);
        etCode.setText("");
        etPIN.setText("");
        elConfirmation.toggleExpansion();
    }

    @Override
    public void setListener(WalletContract.Listener listener) {
        this.listener = listener;
    }
}
