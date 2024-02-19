// Copyright (c) 2022. The Khalti Authors. All rights reserved.

package com.khalti.android.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.khalti.android.demo.composable.DemoScreenV3
import com.khalti.android.demo.theme.KhaltiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KhaltiTheme {
                Surface(
                    Modifier.fillMaxSize(),
                ) {
                    DemoScreenV3()
                }
            }
        }
    }
}



