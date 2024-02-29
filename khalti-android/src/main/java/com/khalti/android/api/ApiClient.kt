/*
 * Copyright (c) 2024. The Khalti Authors. All rights reserved.
 */

package com.khalti.android.api

import android.os.Build
import com.khalti.android.resource.Err
import com.khalti.android.resource.KFailure
import com.khalti.android.resource.Ok
import com.khalti.android.resource.Result
import com.khalti.android.resource.Url
import com.khalti.android.utils.ErrorUtil
import com.khalti.android.v3.CacheManager
import com.khalti.android.v3.Environment
import com.khalti.android.v3.Khalti
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
import java.util.concurrent.TimeUnit

class ApiClient {
    companion object {
        private const val TIME_OUT = 30L

        fun build(): ApiService {
            val khalti = CacheManager.instance().get<Khalti>("khalti")
            assert(khalti != null) {
                "Khalti object has not been cached. There probably an issue in internal logic in the sdk. Please contact the developer"
            }
            val url = if (khalti!!.config.environment == Environment.PROD) {
                Url.BASE_KHALTI_URL_PROD
            } else {
                Url.BASE_KHALTI_URL_STAGING
            }.value

            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val okHttpClient = OkHttpClient.Builder().readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS).addInterceptor {
                    // TODO (Ishwor) Remove temp variables
                    val moduleVersion = ""
                    val packageName = ""
                    val packageVersion = ""
                    val request = it.request().newBuilder()
                        .addHeader("Authorization", "Key ${khalti.config.publicKey}")
                        .addHeader("checkout-version", moduleVersion)
                        .addHeader("checkout-platform", "android")
                        .addHeader("checkout-os-version", Build.VERSION.RELEASE)
                        .addHeader("checkout-device-model", Build.MODEL)
                        .addHeader("checkout-device-manufacturer", Build.MANUFACTURER)
                        .addHeader("merchant-package-name", packageName)
                        .addHeader("merchant-package-version", packageVersion).build()

                    it.proceed(request)
                }.addInterceptor(loggingInterceptor).build()

            return Retrofit.Builder().baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build()
                .create(ApiService::class.java)
        }
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
                is UnknownHostException -> KFailure.ServerUnreachable(
                    t.message ?: "",
                    processedThrowable
                )

                is SocketTimeoutException -> KFailure.NoNetwork(t.message ?: "", processedThrowable)
                is IOException -> KFailure.NoNetwork(t.message ?: "", processedThrowable)
                is HttpException -> {
                    val code = t.code()

                    KFailure.HttpCall(t.message ?: "", processedThrowable, code)
                }

                else -> KFailure.Generic(t.message ?: "", processedThrowable)
            }
            return@withContext Err(failure)
        }
    }
}