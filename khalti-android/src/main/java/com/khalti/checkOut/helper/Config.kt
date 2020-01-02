package com.khalti.checkOut.helper

import androidx.annotation.Keep
import com.khalti.checkOut.api.OnErrorListener
import com.khalti.checkOut.api.OnSuccessListener
import com.khalti.utils.EmptyUtil
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

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
        val additionalData: Map<String, Any>?,
        val paymentPreferences: MutableList<PaymentPreference>?
) : Serializable {

    class Builder(
            private val publicKey: String,
            private val productId: String,
            private val productName: String,
            private val amount: Long
    ) {
        private var productUrl: String? = null
        private var mobile: String? = null
        private var additionalData: MutableMap<String, Any> = HashMap()
        private var paymentPreferences: MutableList<PaymentPreference> = ArrayList()

        private var onSuccessListener: OnSuccessListener? = null
        private var onErrorListener: OnErrorListener? = null

        fun onSuccess(onSuccessListener: OnSuccessListener) = apply { this.onSuccessListener = onSuccessListener }
        fun onError(onErrorListener: OnErrorListener) = apply { this.onErrorListener = onErrorListener }
        fun productUrl(productUrl: String?) = apply { this.productUrl = productUrl }
        fun mobile(mobile: String?) = apply { this.mobile = mobile }
        fun additionalData(additionalData: Map<String, Any>?) = apply {
            if (EmptyUtil.isNotNull(additionalData)) {
                this.additionalData.putAll(additionalData!!)
            }
        }

        fun paymentPreferences(paymentPreferences: List<PaymentPreference>?) = apply {
            if (EmptyUtil.isNotNull(paymentPreferences)) {
                this.paymentPreferences.addAll(paymentPreferences!!)
            }
        }

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