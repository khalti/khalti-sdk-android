/*
 * Copyright (c) 2024. The Khalti Authors. All rights reserved.
 */

package com.khalti.android.service

import com.khalti.android.Khalti
import com.khalti.android.data.PaymentResult
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
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
                            status = it.status ?: "Payment successful", payload = it
                        ), khalti
                    )
                },
                err = {
                    khalti.onMessage.invoke(
                        it.message ?: "", khalti, it.cause, it.code
                    )
                },
            )
        }
    }
}