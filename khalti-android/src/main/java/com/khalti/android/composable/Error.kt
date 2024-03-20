/*
 * Copyright (c) 2024. The Khalti Authors. All rights reserved.
 */

package com.khalti.android.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SignalWifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.khalti.android.resource.ErrorType
import com.khalti.android.utils.ErrorUtil

@Composable
fun KhaltiError(
    errorType: ErrorType,
    title: String? = null,
    message: String? = null,
    icon: ImageVector? = null,
    action: String = "Try Again",
    onAction: (() -> Unit)? = null,
) {
    val resolvedIcon =
        icon ?: if (errorType == ErrorType.network) Icons.Filled.SignalWifiOff else null

    val resolvedTitle = title
        ?: if (errorType == ErrorType.network) "Network unavailable" else null

    val resolvedMessage = message ?: when (errorType) {
        ErrorType.generic -> ErrorUtil.GENERIC_ERROR
        ErrorType.network -> "Make sure your device is connected to the internet and try again"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (resolvedIcon != null) {
            Icon(
                modifier = Modifier
                    .size(56.dp)
                    .padding(bottom = 24.dp),
                imageVector = Icons.Filled.SignalWifiOff,
                contentDescription = "No internet"
            )
        }
        if (!resolvedTitle.isNullOrEmpty()) {
            Text(
                modifier = Modifier.padding(bottom = 16.dp),
                text = resolvedTitle,
                style = MaterialTheme.typography.titleLarge
            )
        }
        Text(
            text = resolvedMessage, style = MaterialTheme.typography.bodyLarge
        )

        if (onAction != null) {
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedButton(onClick = onAction) {
                Text(text = action.uppercase())
            }
        }

    }
}