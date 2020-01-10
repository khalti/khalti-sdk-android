package com.khalti.checkOut.helper

import java.io.Serializable

enum class PaymentPreference(val value: String) : Serializable {
    KHALTI("khalti"),
    SCT("sct"),
    EBANKING("ebanking"),
    MOBILE_BANKING("mobile"),
    CONNECT_IPS("connectips")
}