/*
 * Copyright (c) 2024. The Khalti Authors. All rights reserved.
 */

package com.khalti.android.servicce

import com.khalti.android.api.ApiClient
import com.khalti.android.api.ApiService
import com.khalti.android.v3.KhaltiPayConfig

class VerificationService(config: KhaltiPayConfig) {
    private val apiService: ApiService by lazy {
        ApiClient(config.environment).build(config.publicKey)
    }

    suspend fun verify(pidx: String): Any? {
        try {
            val response = apiService.verify(mapOf("pidx" to pidx))
            val error = response.errorBody()

            if (response.isSuccessful) {
                return response.body()!!
            } else if (error != null) {
                throw Exception(error.toString())
            }
        } catch (_: Exception) {

        }

        return null
    }
}