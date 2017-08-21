package com.khalti.form.EBanking;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.transition.ChangeBounds;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.support.transition.TransitionSet;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.khalti.R;
import com.khalti.carbonX.widget.Button;
import com.khalti.carbonX.widget.FrameLayout;
import com.khalti.carbonX.widget.ProgressBar;
import com.khalti.carbonX.widget.TextInputLayout;
import com.khalti.form.CheckOutActivity;
import com.khalti.form.EBanking.chooseBank.BankChooserActivity;
import com.khalti.form.EBanking.loadBank.BankingActivity;
import com.utila.ActivityUtil;
import com.utila.EmptyUtil;
import com.utila.NetworkUtil;
import com.utila.ResourceUtil;
import com.utila.UserInterfaceUtil;

import java.util.HashMap;
import java.util.List;

import fontana.RobotoMediumTextView;
import fontana.RobotoTextView;

import static android.app.Activity.RESULT_OK;

public class EBanking extends Fragment implements EBankingContract.View {

    private ProgressBar pdLoad;
    private LinearLayout llBank, llMobile;
    private Spinner spBank;
    private FrameLayout flBank;
    private RobotoTextView tvBank, tvBankId;
    private TextInputEditText etMobile;
    private TextInputLayout tilMobile;
    private Button btnPay;

    private FragmentActivity fragmentActivity;
    private EBankingContract.Listener listener;

    private String bankId, bankName;

    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.payment_form, container, false);
        fragmentActivity = getActivity();
        listener = new EBankingPresenter(this);

        pdLoad = mainView.findViewById(R.id.pdLoad);
        llBank = mainView.findViewById(R.id.llBank);
        llMobile = mainView.findViewById(R.id.llMobile);
        spBank = mainView.findViewById(R.id.spBank);
        flBank = mainView.findViewById(R.id.flBank);
        tvBank = mainView.findViewById(R.id.tvBank);
        tvBankId = mainView.findViewById(R.id.tvBankId);
        etMobile = mainView.findViewById(R.id.etMobile);
        tilMobile = mainView.findViewById(R.id.tilMobile);
        btnPay = mainView.findViewById(R.id.btnPay);
        listener.setUpLayout(NetworkUtil.isNetworkAvailable(fragmentActivity));

        btnPay.setOnClickListener(view -> listener.initiatePayment(NetworkUtil.isNetworkAvailable(fragmentActivity), etMobile.getText().toString(), bankId, bankName));
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
    public void toggleProgressBar(boolean show) {
        if (show) {
            pdLoad.setVisibility(View.VISIBLE);
        } else {
            pdLoad.setVisibility(View.GONE);
        }
    }

    @Override
    public void showBankField() {
        llBank.setVisibility(View.VISIBLE);
    }

    @Override
    public void setUpSpinner(Object banks, Object bankIds) {
        spBank.setVisibility(View.VISIBLE);
        flBank.setVisibility(View.GONE);
        ArrayAdapter<String> bankAdapter = new ArrayAdapter<>(fragmentActivity, android.R.layout.simple_list_item_1, (List<String>) banks);

        bankAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);

        spBank.setAdapter(bankAdapter);
        bankId = ((List<String>) bankIds).get(spBank.getSelectedItemPosition());

        spBank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bankId = ((List<String>) bankIds).get(i);
                bankName = ((List<String>) banks).get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void setUpBankItem(String bankName, String bankId) {
        spBank.setVisibility(View.GONE);
        flBank.setVisibility(View.VISIBLE);
        tvBank.setText(bankName);
        tvBankId.setText(bankId);

        this.bankId = bankId;
        flBank.setOnClickListener(view -> listener.openBankList());
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
                    listener.setErrorAnimation();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        } else {
            etMobile.addTextChangedListener(null);
        }
    }

    @Override
    public void toggleButton(boolean enabled) {
        btnPay.setEnabled(enabled);
    }

    @Override
    public void setButtonText(String text) {
        btnPay.setText(text);
    }

    @Override
    public void setErrorAnimation() {
        TransitionSet transitionSet = new TransitionSet();

        Transition errorTransition = new ChangeBounds();
        errorTransition.setInterpolator(new AccelerateDecelerateInterpolator());
        errorTransition.setDuration(400);
        errorTransition.addTarget(tilMobile);

        transitionSet.addTransition(errorTransition);

        TransitionManager.beginDelayedTransition(llMobile, transitionSet);
    }

    @Override
    public void setMobileError(String error) {
        listener.setErrorAnimation();
        tilMobile.setError(error);
    }

    @Override
    public void showNetworkError() {
        UserInterfaceUtil.showSnackBar(fragmentActivity, ((CheckOutActivity) this.fragmentActivity).cdlMain, ResourceUtil.getString(fragmentActivity, R.string.network_error_body),
                false, "", 0, 0, null);
    }

    @Override
    public void showError(String message) {
        UserInterfaceUtil.showSnackBar(fragmentActivity, ((CheckOutActivity) this.fragmentActivity).cdlMain, message,
                true, ResourceUtil.getString(fragmentActivity, R.string.try_again), Snackbar.LENGTH_INDEFINITE, R.color.khaltiAccent, () ->
                        listener.setUpLayout(NetworkUtil.isNetworkAvailable(fragmentActivity)));
    }

    @Override
    public void showMessageDialog(String title, String message) {
        FrameLayout flButton = (FrameLayout) fragmentActivity.getLayoutInflater().inflate(R.layout.component_flat_button, null);
        RobotoMediumTextView tvButton = flButton.findViewById(R.id.tvButton);
        tvButton.setText(ResourceUtil.getString(fragmentActivity, R.string.got_it));

        UserInterfaceUtil.showInfoDialog(fragmentActivity, title, message, true, true, flButton, new UserInterfaceUtil.DialogAction() {
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
    public void openBankList(HashMap<String, Object> dataMap) {
        Intent intent = new Intent(fragmentActivity, BankChooserActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("map", dataMap);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1007);
    }

    @Override
    public void openEBanking(HashMap<String, Object> dataMap) {
        ActivityUtil.openActivity(BankingActivity.class, fragmentActivity, true, dataMap, true);
    }

    @Override
    public void setListener(EBankingContract.Listener listener) {
        this.listener = listener;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1007:
                if (resultCode == RESULT_OK && EmptyUtil.isNotNull(data)) {
                    listener.updateBankItem(data.getStringExtra("name"), data.getStringExtra("id"));
                }
                break;
        }
    }
}
