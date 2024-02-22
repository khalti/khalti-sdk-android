/*
 * Copyright (c) 2024. The Khalti Authors. All rights reserved.
 */

package com.khalti.android.v3

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.khalti.android.PaymentActivity
import com.khalti.android.api.ApiClient
import com.khalti.android.resource.Url
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Though kotlin provides named and optional parameters
// method overloading was required for Java developers
class Khalti private constructor(
    private val context: Context,
    val config: KhaltiPayConfig,
    val onPaymentResult: OnPaymentResult,
    val onMessage: OnMessage,
    val onReturn: OnReturn?,
) {
    var activity: Activity? = null

    companion object {
        fun init(
            context: Context,
            config: KhaltiPayConfig,
            onPaymentResult: OnPaymentResult,
            onMessage: OnMessage,
            onReturn: OnReturn,
        ): Khalti {
            val khalti = Khalti(
                context,
                config,
                onPaymentResult,
                onMessage,
                onReturn,
            )

            CacheManager.instance().put("khalti", khalti)
            return khalti
        }

        fun init(
            context: Context,
            config: KhaltiPayConfig,
            onPaymentResult: OnPaymentResult,
            onMessage: OnMessage,
        ): Khalti {
            val khalti = Khalti(
                context,
                config,
                onPaymentResult,
                onMessage,
                null,
            )

            CacheManager.instance().put("khalti", khalti)

            return khalti
        }
    }

    fun open() {
        val intent = Intent(context, PaymentActivity::class.java)
        context.startActivity(intent)
    }

    fun verify() {
        val apiClient = ApiClient()
        val baseUrl = if (config.isProd()) {
            Url.BASE_KHALTI_URL_PROD
        } else {
            Url.BASE_KHALTI_URL_STAGING
        }
        val call =
            apiClient.build(baseUrl.value, config.publicKey).verify(mapOf("pidx" to config.pidx))

        call.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    Log.i("Response", response.body().toString())
                } else {
                    // TODO (Ishwor) Handle error
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                Log.e("Error", t.printStackTrace().toString())
                // TODO (Ishwor) Handle error
            }
        })
    }

    fun close() {
        val intent = Intent("close_khalti_payment_portal")
        context.sendBroadcast(intent)
    }
}