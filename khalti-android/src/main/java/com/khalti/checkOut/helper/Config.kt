package com.khalti.checkOut.helper

import androidx.annotation.Keep
import com.khalti.checkOut.api.OnCheckOutListener
import com.khalti.checkOut.api.OnErrorListener
import com.khalti.checkOut.api.OnSuccessListener
import java.io.Serializable

@Keep
data class Config(
        val publicKey: String,
        val productId: String,
        val productName: String,
        val amount: Long,

        val onSuccessListener: OnSuccessListener?,
        val onErrorListener: OnErrorListener?,
        val mobile: String?,
        val productUrl: String?,
        val additionalData: HashMap<String, Any>?,
        val paymentPreferences: List<PaymentPreference>?
) : Serializable {

    class Builder(
            private val publicKey: String,
            private val productId: String,
            private val productName: String,
            private val amount: Long
    ) {
        private var productUrl: String? = null
        private var mobile: String? = null
        private var additionalData: HashMap<String, Any>? = null
        private var paymentPreferences: List<PaymentPreference>? = null

        private var onSuccessListener: OnSuccessListener? = null
        private var onErrorListener: OnErrorListener? = null

        fun onSuccess(onSuccessListener: OnSuccessListener) = apply { this.onSuccessListener = onSuccessListener }
        fun onError(onErrorListener: OnErrorListener) = apply { this.onErrorListener = onErrorListener }
        fun productUrl(productUrl: String?) = apply { this.productUrl = productUrl }
        fun mobile(mobile: String?) = apply { this.mobile = mobile }
        fun additionalData(additionalData: HashMap<String, Any>?) = apply { this.additionalData = additionalData }
        fun paymentPreferences(paymentPreferences: List<PaymentPreference>?) = apply { this.paymentPreferences = paymentPreferences }

        fun build() = Config(
                publicKey,
                productId,
                productName,
                amount,
                onSuccessListener,
                onErrorListener,
                productUrl,
                mobile,
                additionalData,
                paymentPreferences
        )
    }
}