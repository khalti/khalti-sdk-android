/*
 * Copyright (c) 2024. The Khalti Authors. All rights reserved.
 */

package com.khalti.android

import android.util.Log

class Khalti private constructor(
    val paymentUrl: String,
    val returnUrl: String,
    val openInKhalti: Boolean,
    val autoVerify: Boolean,
    val publicKey: String,
    val merchantAppDeeplink: String,
    val onComplete: OnComplete,
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

        fun build(): Khalti {
            val assertionConditions: Map<String, Boolean> = mapOf(
                "onComplete" to (onComplete != null),
                "paymentUrl" to (paymentUrl != null),
                "returnUrl" to (returnUrl != null),
                "publicKey" to (publicKey != null),
                "merchantAppDeeplink" to (merchantAppDeeplink != null),
            ).filter { !it.value }

            assert(assertionConditions.isEmpty())
            {
                val requiredParams = assertionConditions.keys.joinToString(separator = ", ")
                val requiredParamsSize = assertionConditions.size
                "$requiredParams ${if (requiredParamsSize > 1) "are" else "is"} required"
            }

            return Khalti(
                paymentUrl = paymentUrl!!,
                returnUrl = returnUrl!!,
                openInKhalti = openInKhalti,
                autoVerify = autoVerify,
                merchantAppDeeplink = merchantAppDeeplink!!,
                publicKey = publicKey!!,
                onComplete = onComplete!!
            )
        }
    }

    fun makePayment() {

    }

    fun verifyPayment() {

    }
}