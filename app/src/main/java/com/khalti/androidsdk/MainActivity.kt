package com.khalti.androidsdk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import com.khalti.android.KhaltiPayConfiguration
import com.khalti.androidsdk.ui.theme.KhaltiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KhaltiTheme {
                Surface(
                    Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DemoScreen(
                        KhaltiPayConfiguration(
                            "https://test-pay.khalti.com/?pidx=Y4riCK7kc2qpoShBPaq7XZ",
                            "https://pay.khalti.com"
                        )
                    )
                }
            }
        }
    }
}



