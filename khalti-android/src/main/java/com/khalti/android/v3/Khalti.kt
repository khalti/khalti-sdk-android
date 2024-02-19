/*
 * Copyright (c) 2024. The Khalti Authors. All rights reserved.
 */

package com.khalti.android.v3

import android.net.Uri

// Though kotlin provides named and optional parameters
// method overloading was required for Java developers
class Khalti private constructor(
    val publicKey: String,
    val pidx: String,
    val returnUrl: Uri,
    val openInKhalti: Boolean,
    val environment: Environment,
    val onPaymentResult: OnPaymentResult,
    val onMessage: OnMessage,
    val onReturn: OnReturn?,
) {
    companion object {
        fun init(
            publicKey: String,
            pidx: String,
            returnUrl: Uri,
            openInKhalti: Boolean = true,
            environment: Environment = Environment.PROD,
            onPaymentResult: OnPaymentResult,
            onMessage: OnMessage,
            onReturn: OnReturn,
        ): Khalti {
            return Khalti(
                publicKey,
                pidx,
                returnUrl,
                openInKhalti,
                environment,
                onPaymentResult,
                onMessage,
                onReturn,
            )
        }

        fun init(
            publicKey: String,
            pidx: String,
            returnUrl: Uri,
            openInKhalti: Boolean = true,
            environment: Environment = Environment.PROD,
            onPaymentResult: OnPaymentResult,
            onMessage: OnMessage,
        ): Khalti {
            return Khalti(
                publicKey,
                pidx,
                returnUrl,
                openInKhalti,
                environment,
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