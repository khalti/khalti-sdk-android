/*
 * Copyright (c) 2024. The Khalti Authors. All rights reserved.
 */

package com.khalti.android.payment

import androidx.lifecycle.ViewModel
import com.khalti.android.Khalti
import com.khalti.android.service.VerificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class KhaltiPaymentState(
    val isLoading: Boolean = true,
    val hasNetwork: Boolean = true,
    val progressDialog: Boolean = false,
)

class KhaltiPaymentViewModel : ViewModel() {
    private val _state = MutableStateFlow((KhaltiPaymentState()))
    val state: StateFlow<KhaltiPaymentState> = _state

    fun verifyPaymentStatus(khalti: Khalti) {
        toggleProgressDialog()
        val verificationRepo = VerificationRepository()
        verificationRepo.verify(khalti.config.pidx, khalti) {
            toggleProgressDialog(false)
        }
    }

    fun toggleNetwork(hasNetwork: Boolean) {
        _state.update { it.copy(hasNetwork = hasNetwork) }
    }


    fun toggleLoading(show: Boolean = true) {
        _state.update { it.copy(isLoading = show) }
    }

    private fun toggleProgressDialog(show: Boolean = true) {
        _state.update { it.copy(progressDialog = show) }
    }
}