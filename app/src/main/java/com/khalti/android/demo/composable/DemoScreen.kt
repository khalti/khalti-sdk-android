// Copyright (c) 2022. The Khalti Authors. All rights reserved.

package com.khalti.android.demo.composable

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.khalti.android.Khalti
import com.khalti.android.data.Environment
import com.khalti.android.data.KhaltiPayConfig
import com.khalti.android.demo.R
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@Preview
fun DemoScreen() {
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember {
        SnackbarHostState()
    }

    val khalti = Khalti.init(
        LocalContext.current,
        KhaltiPayConfig(
            publicKey = "live_public_key_979320ffda734d8e9f7758ac39ec775f",
            pidx = "MoiGc4CvewG9RZmDgUtNTk",
            returnUrl = Uri.parse("https://webhook.site/ed508278-3ce3-4f6d-98f1-0b6084c5c5cd"),
            environment = Environment.TEST
        ),
        onPaymentResult = { paymentResult, khalti ->
            Log.i("Demo | onPaymentResult", paymentResult.toString())
            khalti.close()
            scope.launch {
                snackBarHostState.showSnackbar("Payment successful for pidx: ${khalti.config.pidx}")
            }
        },
        onMessage = { payload ->
            Log.i(
                "Demo | onMessage",
                "${payload.event} ${if (payload.code != null) "(${payload.code})" else ""} | ${payload.message}"
            )
            payload.khalti.close()
            payload.throwable?.printStackTrace()
            scope.launch {
                snackBarHostState.showSnackbar("OnMessage: ${payload.message}")
            }
        },
        onReturn = { _ ->
            Log.i("Demo | onReturn", "OnReturn")
        }
    )

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        content = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Spacer(Modifier.padding(16.dp))
                Image(
                    painterResource(R.mipmap.seru),
                    contentDescription = "Khalti Logo",
                    modifier = Modifier.height(200.dp)
                )
                Spacer(Modifier.height(50.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(text = "Rs. 22", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(8.dp))
                    Text(text = "1 day fee", style = MaterialTheme.typography.bodySmall)
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(
                        {
                            khalti.open()
                        }
                    ) {
                        Text("Pay with Khalti")
                    }
                }
                Spacer(Modifier.height(50.dp))
                Text(
                    text = "pidx: ${khalti.config.pidx}",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(Modifier.height(50.dp))
                Text(
                    text = "This is a demo application developed by some merchant.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
    )
}
