package com.khalti.android

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.webkit.*
import android.widget.Toast
import androidx.annotation.RequiresApi

internal class EPaymentWebClient(
    private val activity: Activity,
    private val returnUrl: String
) : WebViewClient() {

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
        } else {
            Toast.makeText(activity, "Action not permitted", Toast.LENGTH_SHORT).show()
        }

        return true
    }

    private fun handleError(description: String?) {
        val intent = Intent()
        intent.putExtra(OpenKhaltiPay.PAYMENT_URL_LOAD_ERROR_RESULT, description ?: "")

        activity.setResult(OpenKhaltiPay.PAYMENT_URL_LOAD_ERROR, intent)
        activity.finish()
    }
}
