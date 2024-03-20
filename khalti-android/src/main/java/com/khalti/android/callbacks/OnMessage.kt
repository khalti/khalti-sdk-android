/*
 * Copyright (c) 2024. The Khalti Authors. All rights reserved.
 */

package com.khalti.android.callbacks

import com.khalti.android.resource.OnMessagePayload

fun interface OnMessage {
    fun invoke(payload: OnMessagePayload)
}