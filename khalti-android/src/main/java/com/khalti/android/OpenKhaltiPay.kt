package com.khalti.android

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
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

class KhaltiPayConfiguration(val paymentUrl: String, val returnUrl: String) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(paymentUrl)
        parcel.writeString(returnUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<KhaltiPayConfiguration> {
        override fun createFromParcel(parcel: Parcel): KhaltiPayConfiguration {
            return KhaltiPayConfiguration(parcel)
        }

        override fun newArray(size: Int): Array<KhaltiPayConfiguration?> {
            return arrayOfNulls(size)
        }
    }

}

interface PaymentResult

data class PaymentSuccess(
    val pidx: String,
    val amount: Long,
    val mobile: String,
    val purchaseOrderId: String,
    val purchaseOrderName: String,
    val transactionId: String
) : PaymentResult

data class PaymentError(
    val message: String
) : PaymentResult

class PaymentCancelled : PaymentResult