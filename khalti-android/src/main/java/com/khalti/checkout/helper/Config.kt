package com.khalti.checkout.helper

import androidx.annotation.Keep
import com.khalti.checkout.api.OnCheckOutListener
import com.khalti.checkout.api.OnErrorListener
import com.khalti.checkout.api.OnSuccessListener
import com.khalti.utils.EmptyUtil
import com.khalti.utils.LogUtil
import java.io.Serializable
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

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
        val paymentPreferences: MutableList<PaymentPreference>?
) : Serializable {

    @Keep
    class Builder(
            private val publicKey: String,
            private val productId: String,
            private val productName: String,
            private val amount: Long,
            private val onCheckOutListener: OnCheckOutListener
    ) {
        private var productUrl: String? = null
        private var mobile: String? = null
        private var additionalData: MutableMap<String, Any> = HashMap()
        private var paymentPreferences: MutableList<PaymentPreference> = ArrayList()

        fun productUrl(productUrl: String?) = apply { this.productUrl = productUrl }
        fun mobile(mobile: String?) = apply { this.mobile = mobile }
        fun additionalData(additionalData: Map<String, Any>?) = apply {
            if (EmptyUtil.isNotNull(additionalData)) {
                this.additionalData.clear()
                this.additionalData.putAll(additionalData!!)
            }
        }

        fun paymentPreferences(paymentPreferences: List<PaymentPreference>?) = apply {
            if (EmptyUtil.isNotNull(paymentPreferences)) {
                this.paymentPreferences.clear()
                this.paymentPreferences.addAll(paymentPreferences!!)
            }
        }

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