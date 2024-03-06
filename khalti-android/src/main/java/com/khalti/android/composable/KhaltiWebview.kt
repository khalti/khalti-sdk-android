/*
 * Copyright (c) 2024. The Khalti Authors. All rights reserved.
 */

package com.khalti.android.composable

import android.annotation.SuppressLint
import android.net.Uri
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.khalti.android.data.KhaltiPayConfig
import com.khalti.android.resource.Url
import com.khalti.android.view.EPaymentWebClient

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun KhaltiWebView(
    config: KhaltiPayConfig,
    onReturnPageLoaded: () -> Unit,
    onPageLoaded: () -> Unit,
) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.setSupportZoom(true)

                this.webViewClient = EPaymentWebClient(onReturnPageLoaded)
                this.webChromeClient = object : WebChromeClient() {
                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                        if (newProgress == 100) {
                            onPageLoaded()
                        }
                    }
                }
                this.clearCache(true)
            }
        },
        update = {
            val baseUrl = if (config.isProd()) {
                Url.BASE_PAYMENT_URL_PROD
            } else {
                Url.BASE_PAYMENT_URL_STAGING
            }

            val paymentUri =
                Uri.parse(baseUrl.value).buildUpon().appendQueryParameter("pidx", config.pidx)

            it.loadUrl(paymentUri.toString())
        },
    )
}
