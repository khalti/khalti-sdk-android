package com.khalti.checkOut.helper

import java.io.Serializable

enum class PaymentPreference(val value: String) : Serializable {
    WALLET("wallet"),
    SCT("sct"),
    EBANKING("ebanking"),
    MOBILE_BANKING("mobile_banking"),
    CONNECT_IPS("connect_ips")
}