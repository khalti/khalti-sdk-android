/*
 * Copyright (c) 2024. The Khalti Authors. All rights reserved.
 */

package com.khalti.android.api

import com.khalti.android.resource.Err
import com.khalti.android.resource.KFailure
import com.khalti.android.resource.Ok
import com.khalti.android.resource.Result
import com.khalti.android.resource.Url
import com.khalti.android.utils.ErrorUtil
import com.khalti.android.v3.Environment
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okio.IOException
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.net.UnknownHostException

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

suspend fun <T : Any> safeApiCall(
    dispatcher: CoroutineDispatcher, apiCall: suspend () -> Response<T>
): Result<T, KFailure> {
    return withContext(dispatcher) {
        try {
            val response = apiCall.invoke()
            if (response.isSuccessful && response.body() != null) {
                return@withContext Ok(response.body()!!)
            }
            return@withContext Err(
                KFailure.Payment(
                    "Error", Throwable(
                        ErrorUtil.parseError(
                            if (response.errorBody() != null) String(
                                response.errorBody()!!.bytes()
                            ) else "", response.code().toString()
                        )
                    )
                )
            )
        } catch (t: Throwable) {
            val processedThrowable = Throwable(
                ErrorUtil.parseThrowableError(t.message, "600")
            )
            val failure: KFailure = when (t) {
                is UnknownHostException -> KFailure.ServerUnreachable(t.message, processedThrowable)
                is SocketTimeoutException -> KFailure.NoNetwork(t.message, processedThrowable)
                is IOException -> KFailure.NoNetwork(t.message, processedThrowable)
                is HttpException -> {
                    val code = t.code()

                    KFailure.HttpCall(t.message, processedThrowable, code)
                }

                else -> KFailure.Generic(t.message, processedThrowable)
            }
            return@withContext Err(failure)
        }
    }
}