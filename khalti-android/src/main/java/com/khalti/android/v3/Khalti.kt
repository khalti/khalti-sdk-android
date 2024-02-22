/*
 * Copyright (c) 2024. The Khalti Authors. All rights reserved.
 */

package com.khalti.android.v3

import android.content.Context
import android.net.Uri

// Though kotlin provides named and optional parameters
// method overloading was required for Java developers
class Khalti private constructor(
    private val context: Context,
    val config: KhaltiPayConfig,
    val onPaymentResult: OnPaymentResult,
    val onMessage: OnMessage,
    val onReturn: OnReturn?,
) {
    companion object {
        fun init(
            context: Context,
            config: KhaltiPayConfig,
            onPaymentResult: OnPaymentResult,
            onMessage: OnMessage,
            onReturn: OnReturn,
        ): Khalti {
            return Khalti(
                context,
                config,
                onPaymentResult,
                onMessage,
                onReturn,
            )
        }

        fun init(
            context: Context,
            config: KhaltiPayConfig,
            onPaymentResult: OnPaymentResult,
            onMessage: OnMessage,
        ): Khalti {
            return Khalti(
                context,
                config,
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