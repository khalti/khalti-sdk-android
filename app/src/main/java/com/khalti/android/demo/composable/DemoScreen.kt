// Copyright (c) 2022. The Khalti Authors. All rights reserved.

package com.khalti.android.demo.composable

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.khalti.android.demo.R
import com.khalti.android.data.Environment
import com.khalti.android.Khalti
import com.khalti.android.data.KhaltiPayConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoScreen() {
    val khalti = Khalti.init(
        LocalContext.current,
        KhaltiPayConfig(
            "live_secret_key_68791341fdd94846a146f0457ff7b455",
            "4MNRZPhuY8ZvvyRyXqG2fF",
            Uri.parse("https://webhook.site/ed508278-3ce3-4f6d-98f1-0b6084c5c5cd"),
            environment = Environment.TEST
        ),
        onPaymentResult = { paymentResult, khalti ->
            Log.i("Demo | onPaymentResult", paymentResult.toString())
            khalti.close()
        },
        onMessage = { message, khalti, throwable, code ->
            Log.i("Demo | onMessage ${if (code != null) "($code)" else ""}", message)
            khalti.close()
            throwable?.printStackTrace()
        },
        onReturn = { _ ->
            Log.i("Demo | onReturn", "OnReturn")
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
