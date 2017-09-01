package com.khalti.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import khalti.checkOut.api.Config;
import khalti.checkOut.api.OnCheckOutListener;
import khalti.utils.LogUtil;
import khalti.widget.Button;

public class Sample extends AppCompatActivity {

    @BindView(R.id.kpPay0)
    Button kpButton0;
    @BindView(R.id.kpPay)
    Button kpButton;
    @BindView(R.id.kpPay1)
    Button kpButton1;
    @BindView(R.id.kpPay2)
    Button kpButton2;
    @BindView(R.id.kpPay3)
    Button kpButton3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample);
        ButterKnife.bind(this);
        
        String pub = "test_public_key_5c9e36656c90496aaab9c92aa56de430"; //http://192.168.1.103:8000/
//        String pub = "test_public_key_036dc7d983bf4865b388ee5f131962d2"; //http://a.khalti.com/

        Config c = new Config(null, null, null, null, null, null);

        Config config = new Config(pub, "123", "Gaida Churot", "http://churot.com/gaida", 1000L, new OnCheckOutListener() {
            @Override
            public void onSuccess(HashMap<String, Object> data) {
                LogUtil.log("Payment confirmed", data);
            }

            @Override
            public void onError(String action, String message) {
                LogUtil.log(action, message);
            }
        });

        kpButton0.setCheckOutConfig(c);
        kpButton.setCheckOutConfig(config);
        kpButton1.setCheckOutConfig(config);
        kpButton2.setCheckOutConfig(config);
        kpButton3.setCheckOutConfig(config);
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
