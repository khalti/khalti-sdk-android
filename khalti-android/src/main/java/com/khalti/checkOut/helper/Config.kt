package com.khalti.checkOut.helper

import androidx.annotation.Keep
import com.khalti.checkOut.api.OnCheckOutListener
import java.io.Serializable

@Keep
data class Config(
        val publicKey: String,
        val productId: String,
        val productName: String,
        val amount: Long,
        val onCheckOutListener: OnCheckOutListener,

        val mobile: String?,
        val productUrl: String?,
        val additionalData: Map<String, Any>?,
        val paymentPreferences: List<PaymentPreference>?
) : Serializable {

    class Builder(
            private val publicKey: String,
            private val productId: String,
            private val productName: String,
            private val amount: Long,
            private var onCheckOutListener: OnCheckOutListener
    ) {
        private var productUrl: String? = null
        private var mobile: String? = null
        private var additionalData: Map<String, Any>? = null
        private var paymentPreferences: List<PaymentPreference>? = null

        fun productUrl(productUrl: String?) = apply { this.productUrl = productUrl }
        fun mobile(mobile: String?) = apply { this.mobile = mobile }
        fun additionalData(additionalData: Map<String, Any>?) = apply { this.additionalData = additionalData }
        fun paymentPreferences(paymentPreferences: List<PaymentPreference>?) = apply { this.paymentPreferences = paymentPreferences }

        fun build() = Config(
                publicKey,
                productId,
                productName,
                amount,
                onCheckOutListener,
                productUrl,
                mobile,
                additionalData,
                paymentPreferences
        )
    }
}