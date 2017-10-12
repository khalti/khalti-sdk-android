package com.khaltiSample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import khalti.checkOut.api.Config;
import khalti.checkOut.api.OnCheckOutListener;
import khalti.widget.KhaltiButton;

public class Sample extends AppCompatActivity {

    @BindView(R.id.kpPay)
    KhaltiButton khaltiButton;
    @BindView(R.id.kpPay1)
    KhaltiButton khaltiButton1;
    @BindView(R.id.kpPay2)
    KhaltiButton khaltiButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample);
        ButterKnife.bind(this);

        HashMap<String, Object> map = new HashMap<>();
        map.put("merchant_extra", "This is extra data");

        Config config = new Config("live_public_key_91638b9e6167400b897d66fb590942f3", "Product ID", "Product Name", "Product Url", 1000L, map, new OnCheckOutListener() {

            @Override
            public void onSuccess(HashMap<String, Object> data) {
                Log.i("Payment confirmed", data + "");
            }

            @Override
            public void onError(String action, String message) {
                Log.i(action, message);
            }
        });

        khaltiButton.setCheckOutConfig(config);
        khaltiButton.setCustomClickListener(view -> khaltiButton.showCheckOut());

        khaltiButton1.setCheckOutConfig(config);
        khaltiButton2.setCheckOutConfig(config);
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
