package com.khalti.checkout.helper

import androidx.annotation.Keep
import java.io.Serializable

@Keep
enum class PaymentPreference(val value: String) : Serializable {
    KHALTI("khalti"),
    SCT("sct"),
    EBANKING("ebanking"),
    MOBILE_BANKING("mobilecheckout"),
    CONNECT_IPS("connectips")
}