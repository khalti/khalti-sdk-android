/*
 * Copyright (c) 2024. The Khalti Authors. All rights reserved.
 */

package com.khalti.android.v3

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.khalti.android.PaymentActivity
import com.khalti.android.api.ApiClient
import com.khalti.android.resource.Url
import com.khalti.android.servicce.VerificationService
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
            val khalti = Khalti(
                context,
                config,
                onPaymentResult,
                onMessage,
                onReturn,
            )

            CacheManager.instance().put("khalti", khalti)
            return khalti
        }

        fun init(
            context: Context,
            config: KhaltiPayConfig,
            onPaymentResult: OnPaymentResult,
            onMessage: OnMessage,
        ): Khalti {
            val khalti = Khalti(
                context,
                config,
                onPaymentResult,
                onMessage,
                null,
            )

            CacheManager.instance().put("khalti", khalti)

            return khalti
        }
    }

    fun open() {
        val intent = Intent(context, PaymentActivity::class.java)
        context.startActivity(intent)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun verify() {
        val verificationService = VerificationService(config)

        GlobalScope.launch {
            try {
                val result = verificationService.verify(config.pidx)
                Log.i("Payment Result", result?.toString() ?: "")
            } catch (e: Exception) {
                Log.e("Payment Result Error", e.toString())
            }
        }
    }

    fun close() {
        val intent = Intent("close_khalti_payment_portal")
        context.sendBroadcast(intent)
    }
}