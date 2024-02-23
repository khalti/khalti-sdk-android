/*
 * Copyright (c) 2024. The Khalti Authors. All rights reserved.
 */

package com.khalti.android.api

import com.khalti.android.resource.Url
import com.khalti.android.v3.Environment
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal class RetrofitClient(
    private val baseUrl: String,
    private val publicKey: String,
    private val packageName: String,
    private val packageVersion: String,
    private val moduleVersion: String
) {

    val retrofit: Retrofit by lazy {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            /*.addInterceptor {
                val requestBuilder: Request.Builder = it.request().newBuilder()
                requestBuilder.header("Authorization", "Key $publicKey")
                requestBuilder.header("checkout-version", moduleVersion)
                requestBuilder.header("checkout-platform", "android")
                requestBuilder.header("checkout-os-version", Build.VERSION.RELEASE)
                requestBuilder.header("checkout-device-model", Build.MODEL)
                requestBuilder.header("checkout-device-manufacturer", Build.MANUFACTURER)
                requestBuilder.header("merchant-package-name", packageName)
                requestBuilder.header("merchant-package-version", packageVersion)
                requestBuilder.method(it.request().method, it.request().body)

                it.proceed(requestBuilder.build())
            }*/
            .build()

        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
}

class ApiClient(private val environment: Environment = Environment.PROD) {
    private var apiService: ApiService? = null

    fun build(publicKey: String): ApiService {
        val baseUrl = if (environment == Environment.PROD) {
            Url.BASE_KHALTI_URL_PROD
        } else {
            Url.BASE_KHALTI_URL_STAGING
        }

        apiService =
            apiService ?: RetrofitClient(
                baseUrl.value,
                publicKey,
                "com.apple",
                "1.0",
                "3.00.00"
            ).retrofit.create(
                ApiService::class.java
            )
        return apiService!!
    }
}