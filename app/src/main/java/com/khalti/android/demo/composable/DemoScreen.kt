// Copyright (c) 2022. The Khalti Authors. All rights reserved.

package com.khalti.android.demo.composable

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.*
import com.khalti.android.*
import com.khalti.demo.ResultDialog

const val RESULT_TAG = "KHALTI_PAY_RESULT"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoScreen(configuration: KhaltiPayConfiguration) {
    val (result, setResult) = remember { mutableStateOf<PaymentResult?>(null) }

    val khaltiPay = rememberLauncherForActivityResult(OpenKhaltiPay()) {
        setResult(it)

        when (it) {
            is PaymentSuccess -> {
                Log.i(RESULT_TAG, "Payment Success")
            }
            is PaymentError -> {
                Log.i(RESULT_TAG, "Payment Error")
            }
            is PaymentCancelled -> {
                Log.i(RESULT_TAG, "Payment Cancelled")
            }
        }
    }

    if (result != null) {
        ResultDialog(result, setResult)
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Khalti Android SDK Demo")
                }
            )
        },
        content = {
            Column(
                Modifier
                    .padding(it)
                    .fillMaxSize(),
                Arrangement.Center,
                Alignment.CenterHorizontally
            ) {
                FilledTonalButton(
                    {
                        khaltiPay.launch(configuration)
                    }
                ) {
                    Text("Pay with Khalti")
                }
            }
        },
    )
}