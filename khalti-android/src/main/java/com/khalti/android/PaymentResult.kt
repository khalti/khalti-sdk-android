package com.khalti.android

interface PaymentResult

data class PaymentSuccess(
    val pidx: String,
    val amount: Long,
    val mobile: String,
    val purchaseOrderId: String,
    val purchaseOrderName: String,
    val transactionId: String
) : PaymentResult

data class PaymentError(
    val message: String
) : PaymentResult

class PaymentCancelled : PaymentResult
