package com.khalti.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.khalti.form.api.Config;
import com.khalti.widget.basic.Pay;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Sample extends AppCompatActivity {

    @BindView(R.id.kpPay)
    Pay kpPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample);
        ButterKnife.bind(this);

        String pub = "test_public_key_5c9e36656c90496aaab9c92aa56de430";
//        String pub = "test_public_key_036dc7d983bf4865b388ee5f131962d2";
        kpPay.setConfig(new Config(pub, "123", "Gaida Churot", "http://churot.com/gaida", 2000L));
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
