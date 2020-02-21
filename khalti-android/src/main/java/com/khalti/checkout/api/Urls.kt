package com.khalti.checkout.api

enum class Urls(val value: String) {
    WALLET_INITIATE("/api/v2/payment/initiate/"),
    WALLET_CONFIRM("api/v2/payment/confirm/"),
}