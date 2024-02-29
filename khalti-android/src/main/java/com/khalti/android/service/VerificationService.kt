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
import com.khalti.android.v3.PaymentPayload
import kotlinx.coroutines.Dispatchers

class VerificationService {
    private val apiService: ApiService by lazy {
        ApiClient.build()
    }

    suspend fun verify(pidx: String): Result<PaymentPayload, KFailure> {
        return safeApiCall(Dispatchers.IO) {
            apiService.verify(mapOf("pidx" to pidx))
        }
    }
}