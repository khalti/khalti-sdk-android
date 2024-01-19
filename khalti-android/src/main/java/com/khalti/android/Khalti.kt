/*
 * Copyright (c) 2024. The Khalti Authors. All rights reserved.
 */

package com.khalti.android

class KhaltiPayConfig private constructor(
    val paymentUrl: String,
    val returnUrl: String,
    val openInKhalti: Boolean,
    val autoVerify: Boolean,
    val publicKey: String?,
    val merchantAppDeeplink: String?,
    val onComplete: OnComplete?,
) {
    class Builder {
        private var publicKey: String? = null
        private var paymentUrl: String? = null
        private var returnUrl: String? = null
        private var openInKhalti: Boolean = true
        private var autoVerify: Boolean = true
        private var merchantAppDeeplink: String? = null
        private var onComplete: OnComplete? = null

        fun publicKey(publicKey: String): Builder {
            this.publicKey = publicKey
            return this
        }

        fun paymentUrl(paymentUrl: String): Builder {
            this.paymentUrl = paymentUrl
            return this
        }

        fun returnUrl(returnUrl: String): Builder {
            this.returnUrl = returnUrl
            return this
        }

        fun openInKhalti(openInKhalti: Boolean): Builder {
            this.openInKhalti = openInKhalti
            return this
        }

        fun autoVerify(autoVerify: Boolean): Builder {
            this.autoVerify = autoVerify
            return this
        }

        fun merchantAppDeeplink(merchantAppDeeplink: String): Builder {
            this.merchantAppDeeplink = merchantAppDeeplink
            return this
        }

        fun onPaymentComplete(onComplete: OnComplete): Builder {
            this.onComplete = onComplete
            return this
        }

        fun build(): KhaltiPayConfig {
            assert(paymentUrl != null) { "Payment url is required" }
            assert(returnUrl != null) { "Return url is required" }

            assert(openInKhalti || merchantAppDeeplink != null) { "Merchant app's deeplink is required when [openInKhalti] is true" }

            return KhaltiPayConfig(
                paymentUrl = this.paymentUrl!!,
                returnUrl = returnUrl!!,
                openInKhalti = openInKhalti,
                autoVerify = true,
                merchantAppDeeplink = this.merchantAppDeeplink,
                publicKey = this.publicKey,
                onComplete = this.onComplete
            )
        }
    }
}