/*
 * Copyright (c) 2024. The Khalti Authors. All rights reserved.
 */

package com.khalti.android.v3

fun interface OnPaymentResult {
    fun invoke(result: PaymentResult, khalti: Khalti)
}