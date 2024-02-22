// Copyright (c) 2022. The Khalti Authors. All rights reserved.

package com.khalti.android

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.webkit.*
import android.widget.Toast
import androidx.annotation.RequiresApi

internal class EPaymentWebClient : WebViewClient() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?):
            Boolean = handleUriV3(view, request!!.url)

    @SuppressWarnings("deprecation")
    @Deprecated("")
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?):
            Boolean = handleUriV3(view, Uri.parse(url))

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

    private fun handleUriV3(view: WebView?, uri: Uri) : Boolean {
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

    private fun handleUri(view: WebView?, uri: Uri): Boolean {
        val url = uri.toString()
        val path = uri.path
        val fragment = uri.fragment

        val eBankingPath = "/ebanking/initiate/"
        val mPinPath = "/account/transaction_pin"

        if (url == OpenKhaltiPay.DEFAULT_HOME) {
            activity.finish()
        } else if (url.startsWith(returnUrl)) {
            val isSuccess = uri.getQueryParameter("pidx") != null
            val intent = Intent()
            intent.putExtra(OpenKhaltiPay.RESULT, url)

            activity.setResult(
                if (isSuccess) Activity.RESULT_OK else OpenKhaltiPay.ERROR,
                intent
            )
            activity.finish()
        } else if (path.equals(eBankingPath)) {
            view?.loadUrl(url)
        } else if (path.equals(mPinPath) || fragment.equals(mPinPath)) {
            val deeplink = "https://khalti.com/go/?t=mpin"
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(deeplink))

            activity.startActivity(browserIntent)
        } else {
            Toast.makeText(activity, "Action not permitted", Toast.LENGTH_SHORT).show()
        }

        return true
    }
}
