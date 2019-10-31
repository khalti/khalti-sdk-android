package com.khalti.utils

import android.view.View
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

interface ViewUtil {

    companion object {

        fun View.clicks(): Flow<Any?> = callbackFlow {
            this@clicks.setOnClickListener {
                offer("")
            }
            awaitClose { this@clicks.setOnClickListener(null) }
        }
    }
}