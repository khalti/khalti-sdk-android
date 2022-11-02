// Copyright (c) 2022. The Khalti Authors. All rights reserved.

package com.khalti.android.demo.composable

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.khalti.android.*
import com.khalti.android.demo.R

const val RESULT_TAG = "KHALTI_PAY_RESULT"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoScreen() {
    var paymentUrl by remember { mutableStateOf(TextFieldValue("")) }
    var returnUrl by remember { mutableStateOf(TextFieldValue("https://pay.khalti.com")) }

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
            CenterAlignedTopAppBar(
                title = {
                    Text("Khalti Android SDK Demo")
                }

            )
        },
        content = { padding ->
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(Modifier.padding(padding))
                Image(
                    painterResource(R.drawable.khalti_logo_color),
                    contentDescription = "Khalti Logo",
                    modifier = Modifier.height(200.dp)
                )
                OutlinedTextField(
                    value = paymentUrl,
                    label = { Text("Payment URL") },
                    placeholder = { Text("Enter payment URL") },
                    onValueChange = {
                        paymentUrl = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )
                Spacer(Modifier.height(24.dp))
                OutlinedTextField(
                    value = returnUrl,
                    label = { Text("Return URL") },
                    onValueChange = {
                        returnUrl = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )
                Spacer(Modifier.height(40.dp))
                FilledTonalButton(
                    {
                        val config = KhaltiPayConfiguration(
                            paymentUrl.text,
                            returnUrl.text
                        )
                        khaltiPay.launch(config)
                    }
                ) {
                    Text("Pay with Khalti")
                }
            }
        },
    )
}