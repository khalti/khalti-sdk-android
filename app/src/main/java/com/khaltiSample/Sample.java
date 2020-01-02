package com.khaltiSample;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.khalti.checkOut.helper.Config;
import com.khalti.checkOut.helper.KhaltiCheckOut;
import com.khalti.checkOut.helper.PaymentPreference;
import com.khalti.utils.Constant;
import com.khalti.utils.FileStorageUtil;
import com.khalti.utils.LogUtil;
import com.khalti.widget.KhaltiButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

        HashMap<String, Object> map = new HashMap<String, Object>() {{
            put("merchant_extra", "This is extra data");
            put("merchant_extra_2", "This is extra data 2");
        }};

        List<PaymentPreference> ls = new ArrayList<PaymentPreference>() {{
            add(PaymentPreference.EBANKING);
            add(PaymentPreference.WALLET);
        }};
        Config config = new Config.Builder(Constant.pub, "Product ID", "Product Name", 1100L)
                .onSuccess(data -> LogUtil.log("success", data))
                .onError(LogUtil::log)
                .productUrl("Product url")
                .paymentPreferences(ls)
                .additionalData(map)
                .build();


        KhaltiCheckOut khaltiCheckOut = new KhaltiCheckOut(this, config);
        khaltiButton.setOnClickListener(view -> khaltiCheckOut.show());
        khaltiButton1.setCheckOutConfig(config);
        khaltiButton2.setCheckOutConfig(config);
    }

    @OnClick(R.id.btnMore)
    public void onBtnMoreLoadClick() {
        startActivity(new Intent(this, MoreSamples.class));
    }
}