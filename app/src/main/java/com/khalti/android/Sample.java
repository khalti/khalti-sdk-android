package com.khalti.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.utila.LogUtil;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import khalti.form.api.Config;
import khalti.widget.Button;

public class Sample extends AppCompatActivity {

    @BindView(R.id.kpPay)
    Button kpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample);
        ButterKnife.bind(this);

//        String pub = "test_public_key_5c9e36656c90496aaab9c92aa56de430"; //http://192.168.1.103:8000/
        String pub = "test_public_key_036dc7d983bf4865b388ee5f131962d2"; //http://a.khalti.com/
        kpButton.setConfig(new Config(pub, "123", "Gaida Churot", "http://churot.com/gaida", 1000L));
        kpButton.setOnSuccessListener(new Button.OnSuccessListener() {
            @Override
            public void onSuccess(HashMap<String, Object> data) {
                LogUtil.log("Payment confirmed", data);
            }
        });
//        kpButton.setCustomView(getLayoutInflater().inflate(R.layout.component_image_button, null));
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
