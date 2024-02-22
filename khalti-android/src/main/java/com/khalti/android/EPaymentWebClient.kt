// Copyright (c) 2022. The Khalti Authors. All rights reserved.

package com.khalti.android

import android.net.Uri
import android.os.Build
import android.util.Log
import android.webkit.*
import androidx.annotation.RequiresApi
import com.khalti.android.v3.CacheManager
import com.khalti.android.v3.Khalti

internal class EPaymentWebClient : WebViewClient() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?):
            Boolean = handleUri(request!!.url)

    @SuppressWarnings("deprecation")
    @Deprecated("")
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?):
            Boolean = handleUri(Uri.parse(url))

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) = handleError(error?.description.toString())

    @SuppressWarnings("deprecation")
    @Deprecated("")
    override fun onReceivedError(
        view: WebView?,
        errorCode: Int,
        description: String?,
        failingUrl: String?
    ) = handleError(description)

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)

        val khalti = CacheManager.instance().get<Khalti>("khalti")
        val returnUrl = khalti?.config?.returnUrl?.toString() ?: ""

        Log.i("Url", url ?: "")

        if (url?.startsWith(returnUrl) != false) {
            khalti?.onReturn?.invoke()
        }

        khalti?.verify()
    }

    private fun handleUri(uri: Uri): Boolean {
        val url = uri.toString()

        Log.i("Url", url)

        // TODO (Ishwor) Handle redirection to Khalti app for setting MPIN
        // MPIN url : /account/transaction_pin
        return false
    }

    private fun handleError(description: String?) {
        val khalti = CacheManager.instance().get<Khalti>("khalti")
        if (description != null) {
            khalti?.onMessage?.invoke(description)
        }
    }
}
