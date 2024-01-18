/*
 * Copyright (c) 2024. The Khalti Authors. All rights reserved.
 */

package com.khalti.android

class Khalti private constructor(
    val paymentUrl: String,
    val returnUrl: String,
    val openInKhalti: Boolean,
    val publicKey: String?,
    val merchantAppDeeplink: String?,
) {
    class Builder {
        private var publicKey: String? = null
        private var paymentUrl: String? = null
        private var returnUrl: String? = null
        private var openInKhalti: Boolean? = null
        private var merchantAppDeeplink: String? = null

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

        fun merchantAppDeeplink(merchantAppDeeplink: String): Builder {
            this.merchantAppDeeplink = merchantAppDeeplink
            return this
        }

        fun build(): Khalti {
            assert(paymentUrl != null) { "Payment url is required" }
            assert(returnUrl != null) { "Return url is required" }

            val openInKhalti: Boolean = if (this.openInKhalti == null) {
                true
            } else {
                this.openInKhalti!!
            }

            assert(openInKhalti || merchantAppDeeplink != null) { "Merchant app's deeplink is required when [openInKhalti] is true" }

            return Khalti(
                paymentUrl = this.paymentUrl!!,
                returnUrl = returnUrl!!,
                openInKhalti = openInKhalti,
                merchantAppDeeplink = this.merchantAppDeeplink,
                publicKey = this.publicKey
            )
        }
    }
}