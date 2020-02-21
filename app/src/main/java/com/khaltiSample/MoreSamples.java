package com.khaltiSample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.khalti.checkout.helper.OnCheckOutListener;
import com.khalti.checkout.helper.Config;
import com.khalti.checkout.helper.KhaltiCheckOut;
import com.khalti.utils.Constant;
import com.khalti.utils.LogUtil;
import com.khalti.widget.KhaltiButton;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoreSamples extends AppCompatActivity {

    // Butter Knife Injections
    @BindView(R.id.kbDefault)
    KhaltiButton kbDefault;
    @BindView(R.id.kbWithCustomClickListener)
    KhaltiButton kbWithCustomClickListener;
    @BindView(R.id.kbWithCustomView)
    KhaltiButton kbWithCustomView;
    @BindView(R.id.btnWithCustomClickListener)
    Button btnWithCustomClickListener;
    @BindView(R.id.btnWithJavaKhaltiButton)
    Button btnWithJavaKhaltiButton;
    @BindView(R.id.llContainer)
    LinearLayout llContainer;

    private final String TAG = getClass().getSimpleName();
    private Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_samples);
        ButterKnife.bind(this);

        // Require Parameters
        String publicKey = Constant.pub;
        String productId = "product_id";
        String productName = "product_name";
        Long amount = 100L; // In Paisa


        /*
         * Very important
         */
        // Config must me initialize ahead
        config = new Config.Builder(publicKey, productId, productName, amount, new OnCheckOutListener() {
            @Override
            public void onError(@NonNull String action, @NonNull Map<String, String> errorMap) {
                Toast.makeText(MoreSamples.this, "Error " + errorMap, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(@NonNull Map<String, Object> data) {
                Toast.makeText(MoreSamples.this, "Success ", Toast.LENGTH_SHORT).show();
            }
        })
                .build();

        // Init The Buttons
        initKhaltiButton();
        initKhaltiButtonWithCustomClickListener();
        initKhaltiButtonWithCustomView();
        initCustomButtonWithCustomClickListener();
        initKhaltiButtonWithJava();
        initCustomButtonExecuteFromJava();
    }

    void showSuccesss() {
        Toast.makeText(this, "Success ", Toast.LENGTH_SHORT).show();
    }

    /**
     * Using Khalti button
     */
    private void initKhaltiButton() {
        kbDefault.setCheckOutConfig(config);
    }


    /**
     * Using Khalti Button with Custom Click Listener
     */
    private void initKhaltiButtonWithCustomClickListener() {
        // Initialize KhaltiCheckout with config
        final KhaltiCheckOut khaltiCheckOut = new KhaltiCheckOut(this, config);
        // Custom Click Listener
        kbWithCustomClickListener.setOnClickListener(view -> khaltiCheckOut.show());
    }


    /**
     * Using Khalti Button with Custom View
     */
    private void initKhaltiButtonWithCustomView() {
        View view = LayoutInflater.from(this).inflate(R.layout.custom_khalti_button, llContainer, false);
        kbWithCustomView.setCustomView(view);
        kbWithCustomView.setCheckOutConfig(config);
    }


    /**
     * Using Custom Button with Custom Click Listener
     */
    private void initCustomButtonWithCustomClickListener() {
        // Initialize KhaltiCheckout with config
        final KhaltiCheckOut khaltiCheckOut1 = new KhaltiCheckOut(this, config);

        // Adding ClickListener
        btnWithCustomClickListener.setOnClickListener(view -> {
            // Show Widget on click
            khaltiCheckOut1.show();
        });
    }


    /**
     * Using Khalti Button from Java
     */
    private void initKhaltiButtonWithJava() {
        // Initialize KhaltiCheckout with config
        final KhaltiCheckOut khaltiCheckOut2 = new KhaltiCheckOut(this, config);

        // Initialize Khalti Button Programmatically
        final KhaltiButton khaltiButton = new KhaltiButton(this, null);
        // Add the Khalti Button to the Container View Group
        llContainer.addView(khaltiButton);
        // Setting the Custom Click Listener
        khaltiButton.setOnClickListener(view -> {
            // Show The khalti Checkout Activity on Button Click
            khaltiCheckOut2.show();
        });

    }


    /**
     * Custom Button but execution from Java Khalti Button
     */
    private void initCustomButtonExecuteFromJava() {
        // Set Custom Click Listener to the custom Button
        btnWithJavaKhaltiButton.setOnClickListener(view -> {
            // Initialize Khalti Button Programmatically
            final KhaltiButton khaltiButton2 = new KhaltiButton(MoreSamples.this, null);
            // Initialize KhaltiCheckout with config
            final KhaltiCheckOut khaltiCheckOut3 = new KhaltiCheckOut(MoreSamples.this, config);

            // Add Click Listener To Java Khalti Button
            khaltiButton2.setOnClickListener(view1 -> {
                // Show The khalti Checkout Activity on Button Click
                khaltiCheckOut3.show();
            });
            // Loading the Khalti Checkout Activity without the user clicking the khalti Button
            khaltiButton2.performClick();
        });
    }
}
