package com.khalti.form.EBanking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.khalti.R;
import com.khalti.carbonX.widget.Button;
import com.khalti.carbonX.widget.FrameLayout;
import com.khalti.carbonX.widget.ProgressBar;
import com.khalti.carbonX.widget.TextInputLayout;
import com.khalti.form.EBanking.chooseBank.BankChooserActivity;
import com.utila.EmptyUtil;
import com.utila.NetworkUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fontana.RobotoTextView;

import static android.app.Activity.RESULT_OK;

public class EBanking extends Fragment implements EBankingContract.View {

    private ProgressBar pdLoad;
    private LinearLayout llBank;
    private Spinner spBank;
    private FrameLayout flBank;
    private RobotoTextView tvBank, tvBankId;
    private TextInputEditText etMobile;
    private TextInputLayout tilMobile;
    private Button btnPay;

    private FragmentActivity fragmentActivity;
    private EBankingContract.Listener listener;

    private List<String> bankIds = new ArrayList<>();
    private String bankId;

    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.payment_form, container, false);
        fragmentActivity = getActivity();
        listener = new EBankingPresenter(this);

        pdLoad = mainView.findViewById(R.id.pdLoad);
        llBank = mainView.findViewById(R.id.llBank);
        spBank = mainView.findViewById(R.id.spBank);
        flBank = mainView.findViewById(R.id.flBank);
        tvBank = mainView.findViewById(R.id.tvBank);
        tvBankId = mainView.findViewById(R.id.tvBankId);
        etMobile = mainView.findViewById(R.id.etMobile);
        tilMobile = mainView.findViewById(R.id.tilMobile);
        btnPay = mainView.findViewById(R.id.btnPay);

        listener.setUpLayout(NetworkUtil.isNetworkAvailable(fragmentActivity));

        btnPay.setOnClickListener(view -> listener.continuePayment(NetworkUtil.isNetworkAvailable(fragmentActivity), etMobile.getText().toString(), bankId));
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
        this.bankIds = (List<String>) bankIds;

        bankAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);

        spBank.setAdapter(bankAdapter);
        bankId = ((List<String>) bankIds).get(spBank.getSelectedItemPosition());

        spBank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bankId = ((List<String>) bankIds).get(i);
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
    public void setMobileError(String error) {
        tilMobile.setError(error);
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
