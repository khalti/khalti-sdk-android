// Copyright (c) 2022. The Khalti Authors. All rights reserved.

package com.khalti.android.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.khalti.android.demo.composable.DemoScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Surface(
                Modifier.fillMaxSize(),
            ) {
                DemoScreen()
            }
        }
    }
}



