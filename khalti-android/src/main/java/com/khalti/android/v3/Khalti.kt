/*
 * Copyright (c) 2024. The Khalti Authors. All rights reserved.
 */

package com.khalti.android.v3

import android.net.Uri

// Though kotlin provides named and optional parameters
// method overloading was required for Java developers
class Khalti private constructor(
    val publicKey: String,
    val paymentUrl: Uri,
    val returnUrl: Uri,
    val openInKhalti: Boolean,
    val onPaymentResult: OnPaymentResult,
    val onMessage: OnMessage,
    val onReturn: OnReturn?,
) {
    companion object {
        fun init(
            publicKey: String,
            paymentUrl: Uri,
            returnUrl: Uri,
            openInKhalti: Boolean,
            onPaymentResult: OnPaymentResult,
            onMessage: OnMessage,
            onReturn: OnReturn,
        ): Khalti {
            return Khalti(
                publicKey,
                paymentUrl,
                returnUrl,
                openInKhalti,
                onPaymentResult,
                onMessage,
                onReturn,
            )
        }

        fun init(
            publicKey: String,
            paymentUrl: Uri,
            returnUrl: Uri,
            onPaymentResult: OnPaymentResult,
            onMessage: OnMessage,
            onReturn: OnReturn,
        ): Khalti {
            return Khalti(
                publicKey,
                paymentUrl,
                returnUrl,
                true,
                onPaymentResult,
                onMessage,
                onReturn,
            )
        }

        fun init(
            publicKey: String,
            paymentUrl: Uri,
            returnUrl: Uri,
            openInKhalti: Boolean,
            onPaymentResult: OnPaymentResult,
            onMessage: OnMessage,
        ): Khalti {
            return Khalti(
                publicKey,
                paymentUrl,
                returnUrl,
                openInKhalti,
                onPaymentResult,
                onMessage,
                null,
            )
        }

        fun init(
            publicKey: String,
            paymentUrl: Uri,
            returnUrl: Uri,
            onPaymentResult: OnPaymentResult,
            onMessage: OnMessage,
        ): Khalti {
            return Khalti(
                publicKey,
                paymentUrl,
                returnUrl,
                true,
                onPaymentResult,
                onMessage,
                null,
            )
        }
    }

    fun open() {

    }

    fun verify() {

    }

    fun close() {

    }
}