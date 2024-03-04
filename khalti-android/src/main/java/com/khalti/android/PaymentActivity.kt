// Copyright (c) 2022. The Khalti Authors. All rights reserved.

package com.khalti.android

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.webkit.*
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.ProgressBar
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedDispatcher
import androidx.core.os.BuildCompat
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.khalti.android.resource.Url
import com.khalti.android.service.VerificationRepository
import com.khalti.android.cache.Store
import com.khalti.android.view.EPaymentWebClient

internal class PaymentActivity : Activity() {
    private var receiver: BroadcastReceiver? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.gravity = Gravity.CENTER

        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

        val appBar = AppBarLayout(this)
        val toolbar = MaterialToolbar(this)

        val progressBar = ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal)
        progressBar.isIndeterminate = true

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

        setupBackPressListener()

        val khalti = Store.instance().get<Khalti>("khalti")
        if (khalti != null) {
            val config = khalti.config

            webView.webViewClient = EPaymentWebClient {
                val verificationRepo = VerificationRepository()
                progressBar.visibility = ProgressBar.VISIBLE
                verificationRepo.verify(config.pidx, khalti) {
                    runOnUiThread {
                        progressBar.visibility = ProgressBar.GONE
                    }
                }
            }
            webView.webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    progressBar.visibility =
                        if (newProgress == 100) ProgressBar.GONE else ProgressBar.VISIBLE
                }
            }

            val baseUrl = if (config.isProd()) {
                Url.BASE_PAYMENT_URL_PROD
            } else {
                Url.BASE_PAYMENT_URL_STAGING
            }

            val paymentUri = Uri
                .parse(baseUrl.value)
                .buildUpon()
                .appendQueryParameter("pidx", config.pidx)

            webView.clearCache(true)
            webView.loadUrl(paymentUri.toString())

            appBar.addView(toolbar)

            layout.addView(appBar)
            layout.addView(progressBar)
            layout.addView(webView, params)

            setContentView(layout, params)
            registerBroadcast()
        }
    }

    override fun onDestroy() {
        unregisterBroadcast()
        super.onDestroy()
    }

    @Deprecated(
        "Deprecated in Java", ReplaceWith(
            "@Suppress(\"DEPRECATION\") super.onBackPressed()",
            "android.app.Activity"
        )
    )
    override fun onBackPressed() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            onBackAction()
        }
        @Suppress("DEPRECATION")
        super.onBackPressed()
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private fun registerBroadcast() {
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent != null && intent.action.equals("close_khalti_payment_portal")) {
                    finish()
                }
            }
        }
        if (Build.VERSION.SDK_INT >= 26) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                registerReceiver(
                    receiver, IntentFilter("close_khalti_payment_portal"),
                    RECEIVER_NOT_EXPORTED
                )
            } else {
                registerReceiver(
                    receiver, IntentFilter("close_khalti_payment_portal"),
                )
            }
        }
    }

    private fun unregisterBroadcast() {
        unregisterReceiver(receiver)
    }

    private fun setupBackPressListener() {
        if (Build.VERSION.SDK_INT >= 33) {
            val priority = OnBackInvokedDispatcher.PRIORITY_DEFAULT
            onBackInvokedDispatcher.registerOnBackInvokedCallback(priority) {
                onBackAction()
            }
        }
    }

    private fun onBackAction() {
        val khalti = Store.instance().get<Khalti>("khalti")
        khalti?.onMessage?.invoke("User Cancelled", khalti, null, null)
    }
}

