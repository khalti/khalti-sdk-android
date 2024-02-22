// Copyright (c) 2022. The Khalti Authors. All rights reserved.

package com.khalti.android

import android.content.Intent
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
            Boolean = handleUri(view, request!!.url)

    @SuppressWarnings("deprecation")
    @Deprecated("")
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?):
            Boolean = handleUri(view, Uri.parse(url))

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

    private fun handleUri(view: WebView?, uri: Uri) : Boolean {
        val url = uri.toString()
        val khalti = CacheManager.instance().get<Khalti>("khalti")
        val returnUrl = khalti?.config?.returnUrl?.toString() ?: ""

        Log.i("Url", url)

        // TODO (Ishwor) Handle redirection to Khalti app for setting MPIN
        // MPIN url : /account/transaction_pin

        // TODO (Ishwor) Invoke this callback after the page has successfully loaded
        if (url.startsWith(returnUrl)) {
            khalti?.onReturn?.invoke()
        }

        khalti?.verify()

        return false
    }

    private fun handleError(description: String?) {
        val khalti = CacheManager.instance().get<Khalti>("khalti")
        if (description != null) {
            khalti?.onMessage?.invoke(description)
        }
    }
}
