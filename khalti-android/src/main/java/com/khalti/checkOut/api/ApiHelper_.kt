package com.khalti.checkOut.api

import com.google.gson.GsonBuilder
import com.khalti.BuildConfig
import com.khalti.utils.AppUtil
import com.khalti.utils.Constant
import com.khalti.utils.EmptyUtil
import com.khalti.utils.ErrorUtil
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.util.concurrent.TimeUnit

public class ApiHelper_ {

    private val TIME_OUT = 30L
    private var HTTP_STATUS_CODE: Int = 0
    private var HTTP_ERROR: String? = null

    fun apiBuilder(): KhaltiApi {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder()
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                            .addHeader("checkout-version", BuildConfig.VERSION_NAME)
                            .addHeader("checkout-source", "android")
                            .addHeader("checkout-android-version", AppUtil.getOsVersion())
                            .addHeader("checkout-android-api-level", AppUtil.getApiLevel()!!.toString() + "")
                            .build()
                    chain.proceed(request)
                }
                .addInterceptor(interceptor)
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl(Constant.url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()

        return retrofit.create(KhaltiApi::class.java)
    }

    suspend fun <T : Any> callApi(response: Deferred<Response<T>>): Result<T> {
        val apiResponse = response.await()
        try {
            if (apiResponse.isSuccessful)
                return Result.Success(apiResponse.body()!!)

            val error = if (EmptyUtil.isNotNull(apiResponse.errorBody())) String(apiResponse.errorBody()!!.bytes()) else ""
            return Result.Error(Throwable(ErrorUtil.parseError(error)))
        } catch (e: Exception) {
            return Result.Error(if (EmptyUtil.isNotNull(e)) e else Throwable(ErrorUtil.parseError("")))
        }
    }
}