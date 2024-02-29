/*
 * Copyright (c) 2024. The Khalti Authors. All rights reserved.
 */

package com.khalti.android.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers("Authorization: Key live_secret_key_68791341fdd94846a146f0457ff7b455")
    @POST("epayment/lookup/")
    suspend fun verify(@Body body: Map<String, String>): Response<Any>
}