package com.khaltiSample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.khalti.checkout.helper.Config;
import com.khalti.checkout.helper.KhaltiCheckOut;
import com.khalti.checkout.helper.OnCheckOutListener;
import com.khalti.checkout.helper.PaymentPreference;
import com.khalti.utils.Constant;
import com.khalti.utils.LogUtil;
import com.khalti.widget.KhaltiButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Sample extends AppCompatActivity {
    @BindView(R.id.kbPay)
    KhaltiButton khaltiButton;
    @BindView(R.id.btnMore)
    MaterialButton btnMore;
    @BindView(R.id.kbKhalti)
    KhaltiButton kbKhalti;
    @BindView(R.id.kbEBanking)
    KhaltiButton kbEBanking;
    @BindView(R.id.kbMobileBanking)
    KhaltiButton kbMobileBanking;
    @BindView(R.id.kbSct)
    KhaltiButton kbSct;
    @BindView(R.id.kbConnectIps)
    KhaltiButton kbConnectIps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample);
        ButterKnife.bind(this);

        HashMap<String, Object> map = new HashMap<String, Object>() {{
            put("merchant_extra", "This is extra data");
            put("merchant_extra_2", "This is extra data 2");
        }};

        Config mainConfig = new Config.Builder(Constant.pub, "Product ID", "Main", 1100L, new OnCheckOutListener() {
            @Override
            public void onError(@NonNull String action, @NonNull Map<String, String> errorMap) {
                Log.i(action, errorMap.toString());
            }

            @Override
            public void onSuccess(@NonNull Map<String, Object> data) {
                Log.i("success", data.toString());
            }
        })
                .paymentPreferences(new ArrayList<PaymentPreference>() {{
                    add(PaymentPreference.KHALTI);
                    add(PaymentPreference.EBANKING);
                    add(PaymentPreference.MOBILE_BANKING);
                    add(PaymentPreference.CONNECT_IPS);
                    add(PaymentPreference.SCT);
                }})
//                .additionalData(map)
                .build();

        Config khaltiConfig = new Config.Builder(Constant.pub, "Product ID", "Khalti", 1100L, new OnCheckOutListener() {
            @Override
            public void onError(@NonNull String action, @NonNull Map<String, String> errorMap) {
                Log.i(action, errorMap.toString());
            }

            @Override
            public void onSuccess(@NonNull Map<String, Object> data) {
                Log.i("success", data.toString());
            }
        })
                .paymentPreferences(new ArrayList<PaymentPreference>() {{
                    add(PaymentPreference.KHALTI);
                }})
                .additionalData(map)
                .build();

        Config eBankingConfig = new Config.Builder(Constant.pub, "Product ID", "E Banking", 1100L, new OnCheckOutListener() {
            @Override
            public void onError(@NonNull String action, @NonNull Map<String, String> errorMap) {
                Log.i(action, errorMap.toString());
            }

            @Override
            public void onSuccess(@NonNull Map<String, Object> data) {
                Log.i("success", data.toString());
            }
        })
                .paymentPreferences(new ArrayList<PaymentPreference>() {{
                    add(PaymentPreference.EBANKING);
                }})
                .additionalData(map)
                .build();

        Config mBankingConfig = new Config.Builder(Constant.pub, "Product ID", "M Banking", 1100L, new OnCheckOutListener() {
            @Override
            public void onError(@NonNull String action, @NonNull Map<String, String> errorMap) {
                Log.i(action, errorMap.toString());
            }

            @Override
            public void onSuccess(@NonNull Map<String, Object> data) {
                Log.i("success", data.toString());
            }
        })
                .paymentPreferences(new ArrayList<PaymentPreference>() {{
                    add(PaymentPreference.MOBILE_BANKING);
                }})
                .additionalData(map)
                .build();

        Config sctConfig = new Config.Builder(Constant.pub, "Product ID", "SCT", 1100L, new OnCheckOutListener() {
            @Override
            public void onError(@NonNull String action, @NonNull Map<String, String> errorMap) {
                Log.i(action, errorMap.toString());
            }

            @Override
            public void onSuccess(@NonNull Map<String, Object> data) {
                Log.i("success", data.toString());
            }
        })
                .paymentPreferences(new ArrayList<PaymentPreference>() {{
                    add(PaymentPreference.SCT);
                }})
                .additionalData(map)
                .build();

        Config connectIpsConfig = new Config.Builder(Constant.pub, "Product ID", "Connect IPS", 1100L, new OnCheckOutListener() {
            @Override
            public void onError(@NonNull String action, @NonNull Map<String, String> errorMap) {
                Log.i(action, errorMap.toString());
            }

            @Override
            public void onSuccess(@NonNull Map<String, Object> data) {
                Log.i("success", data.toString());
            }
        })
                .paymentPreferences(new ArrayList<PaymentPreference>() {{
                    add(PaymentPreference.CONNECT_IPS);
                }})
                .additionalData(map)
                .build();


        KhaltiCheckOut khaltiCheckOut = new KhaltiCheckOut(this, mainConfig);
        khaltiButton.setOnClickListener(view -> {
            khaltiCheckOut.show();
        });

        KhaltiCheckOut khaltiCheckOut1 = new KhaltiCheckOut(this, khaltiConfig);
        kbKhalti.setOnClickListener(v -> khaltiCheckOut1.show());

        kbEBanking.setCheckOutConfig(eBankingConfig);

        kbMobileBanking.setCheckOutConfig(mBankingConfig);

        kbSct.setCheckOutConfig(sctConfig);

        kbConnectIps.setCheckOutConfig(connectIpsConfig);
    }

    @OnClick(R.id.btnMore)
    public void onBtnMoreLoadClick() {
        startActivity(new Intent(this, MoreSamples.class));
    }
}