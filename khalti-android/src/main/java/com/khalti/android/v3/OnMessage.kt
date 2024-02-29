/*
 * Copyright (c) 2024. The Khalti Authors. All rights reserved.
 */

package com.khalti.android.v3

fun interface OnMessage {
    fun invoke(message: String, throwable: Throwable?, code: Number?)
}