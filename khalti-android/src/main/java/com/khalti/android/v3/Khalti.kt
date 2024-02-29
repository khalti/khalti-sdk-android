/*
 * Copyright (c) 2024. The Khalti Authors. All rights reserved.
 */

package com.khalti.android.v3

import android.content.Context
import android.content.Intent
import android.util.Log
import com.khalti.android.PaymentActivity
import com.khalti.android.resource.KFailure
import com.khalti.android.service.VerificationService
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
        val verificationService = VerificationService()

        GlobalScope.launch {
            val result = verificationService.verify(config.pidx)
            result.match(
                ok = {
                    onPaymentResult.invoke(
                        PaymentResult(
                            status = it.status ?: "Payment successful",
                            payload = it
                        )
                    )
                },
                err = {
                    when (it) {
                        is KFailure.NoNetwork, is KFailure.ServerUnreachable, is KFailure.Generic ->
                            onMessage.invoke(
                                it.message ?: "", it.cause, null
                            )

                        is KFailure.HttpCall -> onMessage.invoke(
                            it.message ?: "", it.cause, it.code
                        )

                        is KFailure.Payment -> onPaymentResult.invoke(
                            PaymentResult(
                                status = "Payment failed",
                                message = it.message ?: "",
                            )
                        )
                    }
                }
            )
        }
    }

    fun close() {
        val intent = Intent("close_khalti_payment_portal")
        context.sendBroadcast(intent)
    }
}