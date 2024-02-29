/*
 * Copyright (c) 2024. The Khalti Authors. All rights reserved.
 */

package com.khalti.android.service

import com.khalti.android.api.ApiClient
import com.khalti.android.api.ApiService
import com.khalti.android.api.safeApiCall
import com.khalti.android.resource.KFailure
import com.khalti.android.resource.Result
import com.khalti.android.v3.KhaltiPayConfig
import kotlinx.coroutines.Dispatchers

class VerificationService(config: KhaltiPayConfig) {
    private val apiService: ApiService by lazy {
        ApiClient(config.environment).build(config.publicKey)
    }

    suspend fun verify(pidx: String): Result<Any, KFailure> {
        return safeApiCall<Any>(Dispatchers.IO) {
            apiService.verify(mapOf("pidx" to pidx))
        }
    }
}