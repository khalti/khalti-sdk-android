// Copyright (c) 2022. The Khalti Authors. All rights reserved.

package com.khalti.android

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.webkit.*
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar

internal class PaymentActivity : Activity() {
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
