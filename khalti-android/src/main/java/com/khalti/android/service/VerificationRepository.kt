/*
 * Copyright (c) 2024. The Khalti Authors. All rights reserved.
 */

package com.khalti.android.service

import com.khalti.android.resource.KFailure
import com.khalti.android.v3.Khalti
import com.khalti.android.v3.PaymentResult
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class VerificationRepository {
    private val verificationService: VerificationService by lazy {
        VerificationService()
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun verify(pidx: String, khalti: Khalti, onComplete: (() -> Unit)? = null) {
        GlobalScope.launch {
            val result = verificationService.verify(pidx)
            onComplete?.invoke()
            result.match(
                ok = {
                    khalti.onPaymentResult.invoke(
                        PaymentResult(
                            status = it.status ?: "Payment successful",
                            payload = it
                        )
                    )
                },
                err = {
                    when (it) {
                        is KFailure.NoNetwork, is KFailure.ServerUnreachable, is KFailure.Generic ->
                            khalti.onMessage.invoke(
                                it.message ?: "", it.cause, null
                            )

                        is KFailure.HttpCall -> khalti.onMessage.invoke(
                            it.message ?: "", it.cause, it.code
                        )

                        is KFailure.Payment -> khalti.onPaymentResult.invoke(
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
}