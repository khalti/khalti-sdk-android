/*
 * Copyright (c) 2024. The Khalti Authors. All rights reserved.
 */

package com.khalti.android.view

import android.net.Uri
import android.os.Build
import android.webkit.*
import androidx.annotation.RequiresApi
import com.khalti.android.cache.Store
import com.khalti.android.Khalti

internal class EPaymentWebClient(val onReturn: () -> Unit) : WebViewClient() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?):
            Boolean = handleUri(request!!.url)

    @SuppressWarnings("deprecation")
    @Deprecated("")
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?):
            Boolean = handleUri(Uri.parse(url))


    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)

        val khalti = Store.instance().get<Khalti>("khalti")
        val returnUrl = khalti?.config?.returnUrl?.toString() ?: ""

        if (url?.startsWith(returnUrl) != false) {
            khalti?.onReturn?.invoke(khalti)
            onReturn()
        }
    }

    private fun handleUri(uri: Uri): Boolean {
        // TODO (Ishwor) Handle redirection to Khalti app for setting MPIN
        val url = uri.toString()
        // MPIN url : /account/transaction_pin
        return false
    }
}
