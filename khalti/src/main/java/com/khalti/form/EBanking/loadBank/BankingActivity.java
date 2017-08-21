package com.khalti.form.EBanking.loadBank;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.khalti.R;
import com.khalti.carbonX.widget.ProgressBar;
import com.utila.EmptyUtil;
import com.utila.LogUtil;
import com.utila.NetworkUtil;
import com.utila.ResourceUtil;

import java.util.HashMap;

import fontana.RobotoTextView;

public class BankingActivity extends AppCompatActivity implements BankContract.View {
    private BankContract.Listener listener;
    private ProgressBar pdLoad;
    private WebView wvBank;
    private ImageView ivMessage;
    private RobotoTextView tvMessage;
    private ScrollView svIndented;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank);
        listener = new BankPresenter(this);

        pdLoad = (ProgressBar) findViewById(R.id.pdLoad);
        wvBank = (WebView) findViewById(R.id.wvBank);
        ivMessage = (ImageView) findViewById(R.id.ivMessage);
        tvMessage = (RobotoTextView) findViewById(R.id.tvMessage);
        svIndented = (ScrollView) findViewById(R.id.svIndented);

        new Handler().postDelayed(() -> listener.setupLayout(NetworkUtil.isNetworkAvailable(this)), 100);
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
    public HashMap<?, ?> receiveArguments() {
        Bundle bundle = getIntent().getBundleExtra("bundle");
        return EmptyUtil.isNotNull(bundle) ? (HashMap<?, ?>) bundle.getSerializable("map") : null;
    }

    @Override
    public void setUpToolbar(String title) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24px);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void setupWebView(String url, String postData) {
        listener.toggleIndentedProgress(true);
        svIndented.setVisibility(View.GONE);
        wvBank.setVisibility(View.VISIBLE);
        wvBank.getSettings().setUseWideViewPort(true);
        wvBank.getSettings().setLoadWithOverviewMode(true);
        wvBank.getSettings().setSupportZoom(true);
        wvBank.getSettings().setBuiltInZoomControls(true);
        wvBank.getSettings().setDisplayZoomControls(false);
        wvBank.getSettings().setJavaScriptEnabled(true);
        wvBank.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                listener.toggleIndentedProgress(false);
                svIndented.setVisibility(View.GONE);
                wvBank.setVisibility(View.VISIBLE);
                LogUtil.log("url", url);
                if (url.contains("khalti.com")) {
                    view.loadUrl("javascript:console.log('MAGIC'+document.getElementsByTagName('html')[0].innerHTML);");
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                if (NetworkUtil.isNetworkAvailable(getApplicationContext())) {
                    listener.showIndentedError(ResourceUtil.getString(getApplicationContext(), R.string.generic_error));
                } else {
                    listener.showIndentedNetworkError();
                }
            }

        });
        wvBank.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                if (consoleMessage.message().startsWith("MAGIC")) {
                    String msg = consoleMessage.message().substring(5); // strip off prefix
                    String message = "<html>\n" +
                            "    <head></head>\n" +
                            "    <body>\n" +
                            "        <div>\n" +
                            "            <script\n" +
                            "                src = \"http://192.168.1.103:8000/static/khalti-widget.js\"\n" +
                            "                data-public_key = \"test_public_key_1104ef89f30b433db0c4d153389c70d1\"\n" +
                            "                data-amount=\"10000\"\n" +
                            "                data-return_url = \"http://192.168.1.103:8080/client/spec/widget/verify.html\"\n" +
                            "                data-button_type = \"mini\"\n" +
                            "                data-product_identity = \"23\"\n" +
                            "                data-product_name = \"An awesome product.\"\n" +
                            "                data-product_url = \"http://localhost:8080/product.html\"\n" +
                            "                data-merchant_stuff1 = \"1\"\n" +
                            "                data-merchant_stuff2 = \"2\">\n" +
                            "            </script>\n" +
                            "        </div>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "        <br>\n" +
                            "    </body>\n" +
                            "</html>";

                    LogUtil.log("data", msg);
                     /* process HTML */
                    return true;
                }

                return false;
            }
        });
//        wvBank.postUrl(url, EncodingUtils.getBytes(postData, "BASE64"));
        wvBank.loadUrl(url);

    }

    @Override
    public void toggleIndentedProgress(boolean show) {
        if (show) {
            svIndented.setVisibility(View.VISIBLE);
            pdLoad.setBackgroundColor(ResourceUtil.getColor(this, R.color.disabled));
            pdLoad.setVisibility(View.VISIBLE);
            tvMessage.setText(ResourceUtil.getString(this, R.string.loading_bank));
        } else {
            svIndented.setVisibility(View.GONE);
            pdLoad.setBackgroundColor(ResourceUtil.getColor(this, R.color.white));
            pdLoad.setVisibility(View.GONE);
        }
    }

    @Override
    public void showIndentedError(String message) {
        wvBank.setVisibility(View.GONE);
        svIndented.setVisibility(View.VISIBLE);
        tvMessage.setText(message);
    }

    @Override
    public void showIndentedNetworkError() {
        wvBank.setVisibility(View.GONE);
        pdLoad.setBackgroundColor(ResourceUtil.getColor(this, R.color.white));
        pdLoad.setVisibility(View.GONE);
        svIndented.setVisibility(View.VISIBLE);
        tvMessage.setText(ResourceUtil.getString(this, R.string.network_error_body));
    }

    @Override
    public void setListener(BankContract.Listener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
