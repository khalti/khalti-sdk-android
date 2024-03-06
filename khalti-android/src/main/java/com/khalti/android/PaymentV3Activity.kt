// Copyright (c) 2022. The Khalti Authors. All rights reserved.

package com.khalti.android

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.webkit.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.khalti.android.composable.KhaltiPaymentPage

internal class PaymentV3Activity : ComponentActivity() {
    private var receiver: BroadcastReceiver? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KhaltiPaymentPage(this)
        }
        registerBroadcast()
    }

    override fun onDestroy() {
        unregisterBroadcast()
        super.onDestroy()
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private fun registerBroadcast() {
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent != null && intent.action.equals("close_khalti_payment_portal")) {
                    finish()
                }
            }
        }
        if (Build.VERSION.SDK_INT >= 26) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                registerReceiver(
                    receiver, IntentFilter("close_khalti_payment_portal"), RECEIVER_NOT_EXPORTED
                )
            } else {
                registerReceiver(
                    receiver, IntentFilter("close_khalti_payment_portal"),
                )
            }
        }
    }

    private fun unregisterBroadcast() {
        unregisterReceiver(receiver)
    }
}

