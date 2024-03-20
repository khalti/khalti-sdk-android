/*
 * Copyright (c) 2024. The Khalti Authors. All rights reserved.
 */

package com.khalti.android.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi


class NetworkUtil {

    companion object {

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun registerListener(context: Context, onNetworkChange: (Boolean) -> Unit) {
            val networkCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    onNetworkChange(true)
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    onNetworkChange(false)
                }
            }

            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                connectivityManager.registerDefaultNetworkCallback(networkCallback)
            } else {
                val networkRequest = NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                    .build()

                connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
            }
        }

        @Suppress("DEPRECATION")
        fun isNetworkAvailable(context: Context): Boolean {
            var result = false

            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val netCapabilities = connectivityManager.activeNetwork ?: return false
                val activeNetworkCapability =
                    connectivityManager.getNetworkCapabilities(netCapabilities) ?: return false

                result = activeNetworkCapability.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        activeNetworkCapability.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        activeNetworkCapability.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            } else {
                connectivityManager.run {
                    connectivityManager.activeNetworkInfo?.run {
                        result = when (type) {
                            ConnectivityManager.TYPE_WIFI -> true
                            ConnectivityManager.TYPE_MOBILE -> true
                            ConnectivityManager.TYPE_ETHERNET -> true
                            else -> false
                        }
                    }
                }
            }

            return result
        }
    }
}