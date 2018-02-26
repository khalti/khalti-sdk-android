package com.khaltiSample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import khalti.checkOut.api.Config;
import khalti.checkOut.api.OnCheckOutListener;
import khalti.checkOut.helper.KhaltiCheckOut;
import khalti.utils.Constant;
import khalti.widget.KhaltiButton;

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
        config = new Config(publicKey, productId, productName, "", amount, new HashMap<>(
        ), new OnCheckOutListener() {
            @Override
            public void onSuccess(HashMap<String, Object> data) {
                Toast.makeText(MoreSamples.this, "Success ", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(String action, String message) {
                Toast.makeText(MoreSamples.this, "Error ", Toast.LENGTH_SHORT).show();
            }
        });

        // Init The Buttons
        initKhaltiButton();
        initKhaltiButtonWithCustomClickListener();
        initKhaltiButtonWithCustomView();
        initCustomButtonWithCustomClickListener();
        initKhaltiButtonWithJava();
        initCustomButtonExecuteFromJava();
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
            khaltiButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Show The khalti Checkout Activity on Button Click
                    khaltiCheckOut3.show();
                }
            });
            // Loading the Khalti Checkout Activity without the user clicking the khalti Button
            khaltiButton2.performClick();
        });
    }
}
