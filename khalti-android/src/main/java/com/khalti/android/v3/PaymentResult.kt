/*
 * Copyright (c) 2024. The Khalti Authors. All rights reserved.
 */

package com.khalti.android.v3

import com.google.gson.annotations.SerializedName

data class PaymentResult(
    val status: String,
    val payload: PaymentPayload?,
    val message: String?
)

data class PaymentPayload(
    @SerializedName("pidx") val pidx: String?,
    @SerializedName("total_amount") val totalAmount: Long = 0,
    @SerializedName("status") val status: String?,
    @SerializedName("transaction_id") val transactionId: String?,
    @SerializedName("fee") val fee: Long = 0,
    @SerializedName("refunded") val refunded: Boolean = false
) {
    override fun toString(): String {
        return StringBuilder()
            .append("pidx: $pidx\n")
            .append("totalAmount: $totalAmount\n")
            .append("status: $status\n")
            .append("transactionId: $transactionId\n")
            .append("fee: $fee\n")
            .append("refunded: $refunded\n")
            .toString()
    }
}