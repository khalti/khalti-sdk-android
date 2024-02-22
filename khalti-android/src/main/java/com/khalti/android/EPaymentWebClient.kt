// Copyright (c) 2022. The Khalti Authors. All rights reserved.

package com.khalti.android

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.webkit.*
import androidx.annotation.RequiresApi

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
        val path = uri.path
        val fragment = uri.fragment

        val mPinPath = "/account/transaction_pin"

        if (url.startsWith(returnUrl)) {
            // callback
        } else if (path.equals(mPinPath) || fragment.equals(mPinPath)) {
            val deeplink = "https://khalti.com/go/?t=mpin"
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(deeplink))

            activity.startActivity(browserIntent)
        }

        return false
    }

    private fun handleError(description: String?) {
        val intent = Intent()
        intent.putExtra(OpenKhaltiPay.PAYMENT_URL_LOAD_ERROR_RESULT, description ?: "")

        activity.setResult(OpenKhaltiPay.PAYMENT_URL_LOAD_ERROR, intent)
        activity.finish()
    }
}
