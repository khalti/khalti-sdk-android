package com.khalti.android

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.KeyEvent
import android.webkit.*
import android.widget.Toast
import androidx.annotation.RequiresApi

internal class EPaymentWebClient(
    private val activity: Activity,
    private val returnUrl: String
) : WebViewClient() {

    override fun onUnhandledKeyEvent(view: WebView?, event: KeyEvent?) {
        super.onUnhandledKeyEvent(view, event)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        return handleUri(request!!.url)
    }

    @SuppressWarnings("deprecation")
    @Deprecated("")
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        return handleUri(Uri.parse(url))
    }

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
}
