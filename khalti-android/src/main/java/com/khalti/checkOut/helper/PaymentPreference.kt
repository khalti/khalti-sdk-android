package com.khalti.checkOut.helper

enum class PaymentPreference(val value: String) {
    WALLET("wallet"),
    SCT("sct"),
    EBANKING("ebanking"),
    MOBILE_BANKING("mobile_banking"),
    CONNECT_IPS("connect_ips")
}