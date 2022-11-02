package com.khalti.android

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.webkit.*
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import androidx.annotation.RequiresApi
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar

class PaymentActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.gravity = Gravity.CENTER

        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

        val appBar = AppBarLayout(this)
        val toolbar = MaterialToolbar(this)
        toolbar.title = "Pay with Khalti"
        toolbar.setNavigationIcon(com.google.android.material.R.drawable.abc_ic_ab_back_material)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        val webView = WebView(this)
        val webSettings = webView.settings

        @SuppressLint("SetJavaScriptEnabled")
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(OpenKhaltiPay.CONFIG, KhaltiPayConfiguration::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(OpenKhaltiPay.CONFIG)
        }?.let { it ->
            webView.webViewClient = EPaymentWebClient(this, it.returnUrl)
            webView.loadUrl(it.paymentUrl)
        }

        appBar.addView(toolbar)

        layout.addView(appBar)
        layout.addView(webView, params)

        setContentView(layout, params)
    }
}

private class EPaymentWebClient(val activity: Activity, val returnUrl: String) : WebViewClient() {

    override fun onUnhandledKeyEvent(view: WebView?, event: KeyEvent?) {
        super.onUnhandledKeyEvent(view, event)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        return handleUri(request!!.url)
    }

    @SuppressWarnings("deprecation")
    @Deprecated("")
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        return handleUri(Uri.parse(url))
    }

    private fun handleUri(uri: Uri): Boolean {
        val url = uri.toString()

        if (url.startsWith(returnUrl)) {
            val isSuccess = uri.getQueryParameter("pidx") != null

            val intent = Intent()
            intent.putExtra(OpenKhaltiPay.RESULT, url)

            activity.setResult(
                if (isSuccess) Activity.RESULT_OK else OpenKhaltiPay.ERROR,
                intent
            )
            activity.finish()
            return true
        }

        return false
    }
}
