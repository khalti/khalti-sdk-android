/*
 * Copyright (c) 2024. The Khalti Authors. All rights reserved.
 */

package com.khalti.android.resource

import com.khalti.android.Khalti

data class OnMessagePayload(
    val event: OnMessageEvent,
    val message: String,
    val khalti: Khalti,
    val throwable: Throwable? = null,
    val code: Number? = null,
    val needsPaymentConfirmation: Boolean = false,
)
