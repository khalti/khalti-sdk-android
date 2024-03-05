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
import android.view.View
import android.webkit.*
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.ProgressBar
import android.window.OnBackInvokedDispatcher
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.khalti.android.resource.Url
import com.khalti.android.service.VerificationRepository
import com.khalti.android.cache.Store
import com.khalti.android.data.KhaltiPayConfig
import com.khalti.android.view.EPaymentWebClient

internal class PaymentActivity : Activity() {
    private var receiver: BroadcastReceiver? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rootLayout = LinearLayout(this)
        rootLayout.orientation = LinearLayout.VERTICAL
        rootLayout.gravity = Gravity.CENTER

        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        val appBar = AppBarLayout(this)

        val progressBar = ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal)
        progressBar.isIndeterminate = true

        setupBackPressListener()

        val khalti = Store.instance().get<Khalti>("khalti")
        if (khalti != null) {
            val config = khalti.config

            appBar.addView(toolbar())

            rootLayout.addView(appBar)
            rootLayout.addView(progressBar)
            rootLayout.addView(
                webView(
                    config,
                    onLoadComplete = {
                        progressBar.visibility =
                            if (it == 100) ProgressBar.GONE else ProgressBar.VISIBLE
                    },
                    onReturn = {
                        val verificationRepo = VerificationRepository()
                        progressBar.visibility = ProgressBar.VISIBLE
                        verificationRepo.verify(config.pidx, khalti) {
                            runOnUiThread {
                                progressBar.visibility = ProgressBar.GONE
                            }
                        }

                    },
                ), params
            )

            setContentView(rootLayout, params)
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

    // ---------------------------- UI ---------------------------------- //

    private fun toolbar(): View {
        val toolbar = MaterialToolbar(this)
        toolbar.title = "Pay with Khalti"
        toolbar.setNavigationIcon(com.google.android.material.R.drawable.abc_ic_ab_back_material)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        return toolbar
    }

    private fun webView(
        config: KhaltiPayConfig,
        onLoadComplete: (progress: Int) -> Unit,
        onReturn: () -> Unit
    ): View {
        val webView = WebView(this)
        val webSettings = webView.settings

        @SuppressLint("SetJavaScriptEnabled")
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true

        webView.webViewClient = EPaymentWebClient(onReturn)
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                onLoadComplete(newProgress)
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

        return webView
    }
}

