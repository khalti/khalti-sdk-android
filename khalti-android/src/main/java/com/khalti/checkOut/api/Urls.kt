package com.khalti.checkOut.api

enum class Urls(val value: String) {
    WALLET_INITIATE("/api/v2/payment/initiate/"),
    WALLET_CONFIRM("api/v2/payment/confirm/"),
}