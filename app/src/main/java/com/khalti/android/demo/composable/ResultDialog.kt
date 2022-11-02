// Copyright (c) 2022. The Khalti Authors. All rights reserved.

package com.khalti.android.demo.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.khalti.android.*

@Composable
fun ResultDialog(result: PaymentResult, setResult: (PaymentResult?) -> Unit) {
    AlertDialog(
        title = {
            Text(
                when (result) {
                    is PaymentSuccess -> "Payment Success"
                    is PaymentError -> "Payment Error"
                    is PaymentCancelled -> "Payment Cancelled"
                    else -> "Unknown"
                }
            )
        },
        text = {
            when (result) {
                is PaymentSuccess -> Column {
                    Text("Identifier: ${result.pidx}")
                    Text("Amount: ${result.amount}")
                    Text("Mobile: ${result.mobile}")
                    Text("Purchase Order ID: ${result.purchaseOrderId}")
                    Text("Purchase Order Name: ${result.purchaseOrderName}")
                    Text("Transaction ID: ${result.transactionId}")
                }
                is PaymentError -> Text(result.message)
                is PaymentCancelled -> Text("The payment was cancelled.")
            }
        },
        confirmButton = {
            FilledTonalButton(
                onClick = {
                    setResult(null)
                }
            ) {
                Text("OK")
            }
        },
        onDismissRequest = {
            setResult(null)
        },
    )
}
