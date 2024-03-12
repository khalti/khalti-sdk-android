/*
 * Copyright (c) 2024. The Khalti Authors. All rights reserved.
 */

package com.khalti.android.payment

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khalti.android.Khalti
import com.khalti.android.resource.Url
import com.khalti.android.service.VerificationRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class KhaltiPaymentState(
    val isLoading: Boolean = true,
    val hasNetwork: Boolean = true,
)

class KhaltiPaymentViewModel() : ViewModel() {
    private val _state = MutableStateFlow((KhaltiPaymentState()))
    val state: StateFlow<KhaltiPaymentState> = _state

    fun showLoading() {
        _state.update { it.copy(isLoading = true) }
    }

    fun hideLoading() {
        _state.update { it.copy(isLoading = false) }
    }

    fun verifyPaymentStatus(khalti: Khalti) {
        // TODO (Ishwor) Show progress
        val verificationRepo = VerificationRepository()
        verificationRepo.verify(khalti.config.pidx, khalti) {
            /*no-op*/
        }
    }

    fun toggleNetwork(hasNetwork: Boolean) {
        _state.update { it.copy(hasNetwork = hasNetwork) }
    }
}