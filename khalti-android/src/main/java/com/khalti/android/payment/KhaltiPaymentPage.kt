/*
 * Copyright (c) 2024. The Khalti Authors. All rights reserved.
 */

package com.khalti.android.payment

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.khalti.android.Khalti
import com.khalti.android.cache.Store
import com.khalti.android.composable.KhaltiError
import com.khalti.android.composable.KhaltiWebView
import com.khalti.android.resource.ErrorType
import com.khalti.android.resource.OnMessageEvent
import com.khalti.android.resource.OnMessagePayload
import com.khalti.android.utils.NetworkUtil

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KhaltiPaymentPage(activity: Activity, viewModel: KhaltiPaymentViewModel) {
    val state by viewModel.state.collectAsState()
    val recomposeState = mutableStateOf(false)
    Scaffold(
        topBar = {
            Surface(shadowElevation = 4.dp) {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = {
                            onBack()
                            activity.finish()
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    title = {
                        Text(text = "Payment Gateway")
                    },
                    actions = {
                        IconButton(onClick = {
                            recomposeState.value = !recomposeState.value
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Refresh,
                                contentDescription = "Refresh"
                            )
                        }
                    },
                )
            }
        },
    ) {
        Surface(modifier = Modifier.padding(top = it.calculateTopPadding())) {
            val khalti = Store.instance().get<Khalti>("khalti")
            if (khalti != null) {
                val config = khalti.config
                if (state.hasNetwork) {
                    Box(
                        Modifier
                            .fillMaxSize()
                    ) {
                        KhaltiWebView(
                            config = config,
                            onReturnPageLoaded = {
                                viewModel.verifyPaymentStatus(khalti)

                            },
                            onPageLoaded = {
                                viewModel.hideLoading()
                            },
                        )
                        if (state.isLoading) {
                            LinearProgressIndicator(
                                Modifier
                                    .height(6.dp)
                                    .width(200.dp)
                                    .align(Alignment.Center),
                                color = Color.Gray
                            )
                        }
                    }

                } else {
                    KhaltiError(errorType = ErrorType.network) {
                        viewModel.toggleNetwork(NetworkUtil.isNetworkAvailable(activity))
                    }
                }
            }
        }

    }

    LaunchedEffect(state.hasNetwork && recomposeState.value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            NetworkUtil.registerListener(activity) {
                viewModel.toggleNetwork(it)
            }
        }
    }
}

fun onBack() {
    val khalti = Store.instance().get<Khalti>("khalti")
    khalti?.onMessage?.invoke(
        OnMessagePayload(
            OnMessageEvent.BackPressed,
            "User pressed back",
            khalti,
            needsPaymentConfirmation = true
        )
    )
}
