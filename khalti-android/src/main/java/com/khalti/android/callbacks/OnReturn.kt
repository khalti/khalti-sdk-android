/*
 * Copyright (c) 2024. The Khalti Authors. All rights reserved.
 */

package com.khalti.android.callbacks

import com.khalti.android.Khalti

fun interface OnReturn {
    fun invoke(khalti: Khalti)
}