package com.khaltiSample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.khalti.checkOut.helper.Config;
import com.khalti.checkOut.api.OnCheckOutListener;
import com.khalti.checkOut.helper.PaymentPreference;
import com.khalti.utils.Constant;
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
    @BindView(R.id.kbPay1)
    KhaltiButton khaltiButton1;
    @BindView(R.id.kbPay2)
    KhaltiButton khaltiButton2;
    @BindView(R.id.btnMore)
    MaterialButton btnMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample);
        ButterKnife.bind(this);

        HashMap<String, String> map = new HashMap<>();
        map.put("merchant_extra", "This is extra data");
        map.put("merchant_extra_2", "This is extra data 2");

        Config config = new Config.Builder(Constant.pub, "Product ID", "Product Name", 1100L, new OnCheckOutListener() {
            @Override
            public void onSuccess(@NonNull Map<String, ?> data) {
                Log.i("Payment confirmed", data + "");

            }

            @Override
            public void onError(@NonNull String action, @NonNull String message) {
                Log.i(action, message);
            }
        })
                .productUrl("Product url")
                .paymentPreferences(new ArrayList<PaymentPreference>() {{
                    add(PaymentPreference.WALLET);
                    add(PaymentPreference.EBANKING);
                }})
                .additionalData(map)
                .build();

        khaltiButton.setOnClickListener(view -> khaltiButton.showCheckOut(config));
        khaltiButton1.setCheckOutConfig(config);
        khaltiButton2.setCheckOutConfig(config);
    }

    @OnClick(R.id.btnMore)
    public void onBtnMoreLoadClick() {
        startActivity(new Intent(this, MoreSamples.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}