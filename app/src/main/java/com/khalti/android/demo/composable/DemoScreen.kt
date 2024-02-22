// Copyright (c) 2022. The Khalti Authors. All rights reserved.

package com.khalti.android.demo.composable

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.khalti.android.KhaltiPayConfiguration
import com.khalti.android.OpenKhaltiPay
import com.khalti.android.PaymentCancelled
import com.khalti.android.PaymentError
import com.khalti.android.PaymentResult
import com.khalti.android.PaymentSuccess
import com.khalti.android.demo.R
import com.khalti.android.v3.Environment
import com.khalti.android.v3.Khalti
import com.khalti.android.v3.KhaltiPayConfig

const val RESULT_TAG = "KHALTI_PAY_RESULT"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoScreen() {
    var paymentUrl by remember { mutableStateOf(TextFieldValue("")) }
    var returnUrl by remember { mutableStateOf(TextFieldValue("https://redirect.khalti.com")) }

    var urlErrorMessage by remember { mutableStateOf("") }

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
                        urlErrorMessage = ""
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    isError = urlErrorMessage.isNotEmpty(),
                    supportingText = { Text(urlErrorMessage) }
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
                        try {
                            val config = KhaltiPayConfiguration(
                                paymentUrl.text,
                                returnUrl.text
                            )
                            khaltiPay.launch(config)
                        } catch (e: Exception) {
                            val message = e.message ?: ""

                            when (e) {
                                is IllegalArgumentException, is IllegalStateException -> {
                                    urlErrorMessage = message
                                }

                                else -> Log.e(RESULT_TAG, message)
                            }
                        }
                    }
                ) {
                    Text("Pay with Khalti")
                }
            }
        },
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoScreenV3() {

    val khalti = Khalti.init(
        LocalContext.current,
        KhaltiPayConfig(
            "b476aa4b21864b54ab96e430c9192be1",
            "2fpAfWxK3S6coXpQkeXVRb",
            Uri.parse("https://khalti.com"),
            environment = Environment.TEST
        ),
        {},
        {},
        onReturn = {
            Log.i("Demo", "OnReturn")
        }
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Khalti Android SDK Demo V3")
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
                FilledTonalButton(
                    {
                        khalti.open()
                    }
                ) {
                    Text("Pay with Khalti")
                }
            }
        },
    )
}
