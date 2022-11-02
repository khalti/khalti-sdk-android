// Copyright (c) 2022. The Khalti Authors. All rights reserved.

package com.khalti.android

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract

class OpenKhaltiPay : ActivityResultContract<KhaltiPayConfiguration, PaymentResult>() {
    override fun createIntent(context: Context, input: KhaltiPayConfiguration): Intent {
        return Intent(context, PaymentActivity::class.java).apply {
            putExtra(CONFIG, input)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): PaymentResult {
        val url = intent?.getStringExtra(RESULT)
            ?: return PaymentCancelled()

        with(Uri.parse(url)) {
            return when (resultCode) {
                Activity.RESULT_OK -> PaymentSuccess(
                    getQueryParameter("pidx") ?: "",
                    getQueryParameter("amount")?.toLongOrNull() ?: 0,
                    getQueryParameter("mobile") ?: "",
                    getQueryParameter("purchase_order_id") ?: "",
                    getQueryParameter("purchase_order_name") ?: "",
                    getQueryParameter("transaction_id") ?: "",
                )
                ERROR -> PaymentError(
                    getQueryParameter("message") ?: "Payment Failed",
                )
                else -> PaymentCancelled()
            }
        }
    }

    companion object {
        const val CONFIG = "config"
        const val RESULT = "payment-result"
        const val ERROR = -2
    }
}

