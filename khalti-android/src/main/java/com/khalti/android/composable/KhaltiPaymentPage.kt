/*
 * Copyright (c) 2024. The Khalti Authors. All rights reserved.
 */

package com.khalti.android.composable

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.khalti.android.Khalti
import com.khalti.android.cache.Store
import com.khalti.android.resource.ErrorType
import com.khalti.android.service.VerificationRepository
import com.khalti.android.utils.NetworkUtil

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KhaltiPaymentPage(activity: Activity) {
    val showProgress = remember {
        mutableStateOf(true)
    }
    val networkAvailable = remember {
        mutableStateOf(NetworkUtil.isNetworkAvailable(activity))
    }

    val pageLoaded = remember {
        mutableStateOf(false)
    }
    val reloadPage = remember {
        mutableStateOf(NetworkUtil.isNetworkAvailable(activity))
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        NetworkUtil.registerListener(activity) {
            networkAvailable.value = it
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {
                        onBackAction()
                        activity.finish()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                title = {
                    Text(text = "Pay With Khalti")
                },
            )
        },
    ) {
        Surface(modifier = Modifier.padding(top = it.calculateTopPadding())) {
            val khalti = Store.instance().get<Khalti>("khalti")
            if (khalti != null) {
                val config = khalti.config
                val webView: @Composable () -> Unit = {
                    KhaltiWebView(
                        config = config,
                        onReturnPageLoaded = {
                            showProgress.value = true
                            val verificationRepo = VerificationRepository()
                            verificationRepo.verify(config.pidx, khalti) {
                                activity.runOnUiThread {
                                    showProgress.value = false
                                }
                            }

                        },
                        onPageLoaded = {
                            showProgress.value = false
                        },
                    )

                }
                if (networkAvailable.value) {
                    if (showProgress.value) {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .background(Color.LightGray)
                        ) {
                            webView()
                            LinearProgressIndicator(
                                Modifier
                                    .fillMaxWidth()
                                    .height(68.dp)
                                    .align(Alignment.TopCenter),
                                color = Color.Gray
                            )
                        }
                    } else {
                        webView()
                    }
                } else {
                    KhaltiError(errorType = ErrorType.network) {

                    }
                }
            }
        }

    }
}

private fun onBackAction() {
    val khalti = Store.instance().get<Khalti>("khalti")
    khalti?.onMessage?.invoke("User Cancelled", khalti, null, null)
}
