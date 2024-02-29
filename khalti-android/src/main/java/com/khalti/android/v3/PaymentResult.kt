/*
 * Copyright (c) 2024. The Khalti Authors. All rights reserved.
 */

package com.khalti.android.v3

/// Status
/// statusCode [1] -> status [Payment Submitted]
/// statusCode [36] -> status [Cancelled] (Cancelled by user)

data class PaymentResult(
    val status: String,
    val payload: PaymentPayload?,
    val message: String?
)

data class PaymentPayload(
    val pidx: String,
    val amount: Long,
    val transactionId: String
)