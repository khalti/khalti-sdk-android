/*
 * Copyright (c) 2024. The Khalti Authors. All rights reserved.
 */

package com.khalti.android.callbacks

import com.khalti.android.Khalti
import com.khalti.android.data.PaymentResult

fun interface OnPaymentResult {
    fun invoke(result: PaymentResult, khalti: Khalti)
}